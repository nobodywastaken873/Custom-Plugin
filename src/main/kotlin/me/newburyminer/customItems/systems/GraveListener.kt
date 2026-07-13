package me.newburyminer.customItems.systems

import io.papermc.paper.datacomponent.DataComponentTypes
import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.addItemorClaim
import me.newburyminer.customItems.Utils.Companion.getCustom
import me.newburyminer.customItems.Utils.Companion.getListTag
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.removeTag
import me.newburyminer.customItems.Utils.Companion.serializeAsBytes
import me.newburyminer.customItems.Utils.Companion.setListTag
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.gui.combat.GraveItemsGui
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomEnchantments
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.*
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

object GraveListener: Listener {
    @EventHandler
    fun onPlayerDeath(e: PlayerDeathEvent) {
        createGrave(e)
    }
    private val graveMarkerId = "gravemarker"
    private fun createGrave(e: PlayerDeathEvent) {
        // soulbound, perm consumable to keep xp
        // change settings for custom worlds
        // prevent chest from being broken,
        if (e.player.world == CustomItems.bossWorld) {
            e.keepInventory = true
            e.keepLevel = true
            e.drops.clear()
            e.droppedExp = 0
            return
        }

        // if its in boss world, cancel
        if (e.player.world != Bukkit.getServer().worlds[0] && e.player.world != Bukkit.getServer().worlds[1]
            && e.player.world != Bukkit.getServer().worlds[2] && e.player.world != CustomItems.aridWorld) return
        if (e.isCancelled) return

        // Dont do it if the player has no items
        if (e.drops.isEmpty()) return
        // Finding location
        val loc = findLoc(e.player.location)
        if (loc == null) {
            e.keepInventory = true
            e.drops.clear()
            return
        }

        // Get previous grave list, update it with new location added at end for /graves
        val graveList = (e.player.getListTag<Location>("gravelist") ?: listOf()).toMutableList()
        graveList.add(loc.world.getBlockAt(loc).location)
        e.player.setListTag("gravelist", graveList)
        // Spawn a block display, interaction entity, and text display to use for the grave
        val chestDisplay: BlockDisplay = e.player.world.spawn(loc.world.getBlockAt(loc).location, BlockDisplay::class.java)
        val armorStand: Interaction = e.player.world.spawn(loc.world.getBlockAt(loc).location.add(0.5, 0.0, 0.5), Interaction::class.java) {
            it.interactionHeight = 1.0F
            it.interactionWidth = 1.0F
        }
        val textDisplay: TextDisplay = e.player.world.spawn(loc.world.getBlockAt(loc).location.add(0.5, 1.2, 0.5), TextDisplay::class.java)
        // remove anything not to be dropped here
        val drops = e.drops.toMutableList()
        setGraveTags(e.player, drops)
        for (item in drops.toList()) {
            if (CustomEnchantments.SOULBOUND in item.enchantments) {
                e.itemsToKeep.add(item)
                drops.remove(item)
            }
        }

        armorStand.setListTag("graveitems", e.drops.toMutableList())
        // can change this later to for loop and skip anything
        saveGrave(e, e.drops.toMutableList())
        e.drops.clear()

        setData(chestDisplay, armorStand, textDisplay)
        armorStand.setTag("id", graveMarkerId)
        armorStand.setTag("currentlyopen", false)
        armorStand.setTag("owner", e.player.uniqueId)
        if (e.damageSource.causingEntity is Player) {
            armorStand.setTag("killer", (e.damageSource.causingEntity as Player).uniqueId)
            armorStand.setTag("looted", 1)
        } else if (e.player.lastDamageCause != null && e.player.lastDamageCause!!.damageSource.causingEntity is Player) {
            armorStand.setTag("killer", (e.player.lastDamageCause!!.damageSource.causingEntity as Player).uniqueId)
            armorStand.setTag("looted", 1)
        }
        textDisplay.text(text("${e.player.name}'s grave", arrayOf(199, 4, 30)))
        e.player.setTag("gravetpcooldown", (5 * 60 * 1000 + System.currentTimeMillis()))
    }
    private fun saveGrave(e: PlayerDeathEvent, drops: MutableList<ItemStack>) {

        val folderPath = System.getProperty("user.dir") + "/plugins/customItems/"
        val directory = File(folderPath)
        if (!directory.exists()) { directory.mkdir() }

        val fileName = folderPath + "savedGraves.txt"
        val file = File("plugins/customItems/savedGraves.txt")
        if (!file.exists()) { file.createNewFile() }

        val locBytes = Base64.getEncoder().encodeToString(e.player.location.serializeAsBytes())
        val itemBytes = Base64.getEncoder().encodeToString(drops.first().serializeAsBytes())
        var totalStr = ("NEWLINE${e.player.name},${ZonedDateTime.now(ZoneId.systemDefault())}LOCATION${locBytes}")
        for (item in drops) {
            totalStr += "ITEMSTACK${Base64.getEncoder().encodeToString(item.serializeAsBytes())}"
        }
        //e.player.sendMessage(locBytes)
        //e.player.sendMessage(itemBytes)
        totalStr += "\n"
        val total = totalStr.toByteArray()
        try {
            Files.write(Paths.get(fileName), total, StandardOpenOption.APPEND)
        } catch (e: IOException) {
            Bukkit.getLogger().info(e.toString())}
    }
    private fun setGraveTags(player: Player, drops: MutableList<ItemStack>) {
        val equipment = listOf(
            player.equipment.helmet to 0,
            player.equipment.chestplate to 1,
            player.equipment.leggings to 2,
            player.equipment.boots to 3,
            player.equipment.itemInOffHand to 4,
        )

        for ((item, graveslot) in equipment) {
            if (CustomEnchantments.SOULBOUND in item.enchantments) continue
            if (item.type == Material.AIR) continue

            val match = drops.find { item == it } ?: continue
            match.setTag("graveslot", graveslot)
        }
    }
    private fun setData(block: BlockDisplay, interaction: Interaction, textDisplay: TextDisplay) {
        block.isInvulnerable = true
        block.block = Material.CHEST.createBlockData()
        block.displayWidth = 1F
        block.displayHeight = 1F
        block.setGravity(false)
        block.setNoPhysics(true)
        interaction.isInvulnerable = true
        interaction.isInvisible = true
        interaction.setGravity(false)
        interaction.setNoPhysics(true)
        textDisplay.billboard = Display.Billboard.CENTER
    }
    private fun findLoc(startLoc: Location): Location? {
        val loc = startLoc.clone()
        // If they die in the void, shift it to the lowest possible location
        when (loc.world) {
            Bukkit.getServer().worlds[0] -> {
                if (loc.y < -64.0) loc.y = -64.0
            }
            CustomItems.aridWorld -> {
                if (loc.y < -256.0) loc.y = -256.0
            }
            else -> {
                if (loc.y < 0.0) loc.y = 0.0
            }
        }
        // If inside of a block, start moving it up until you reach an empty block
        if (!loc.world.getBlockAt(loc).isPassable) {
            while (!loc.world.getBlockAt(loc).isPassable) {
                loc.y += 1
                if (loc.y >= 319) {
                    return null
                }
            }
        }
        // Otherwise, move it down to the first nonpassable block
        else {
            while (loc.world.getBlockAt(loc).isPassable) {
                loc.y -= 1
            }
            loc.y += 1
        }

        return loc.clone()
    }

    @EventHandler
    fun onPlayerInteractThing(e: PlayerInteractAtEntityEvent) {
        openGrave(e)
    }
    private fun openGrave(e: PlayerInteractAtEntityEvent) {
        // Is interaction grave
        if (e.rightClicked !is Interaction) return
        if (e.rightClicked.getTag<String>("id") != graveMarkerId) return

        val interaction: Interaction = e.rightClicked as Interaction
        // Ensure another player does not have it open in a GUI
        if (interaction.getTag<Boolean>("currentlyopen") == true) {e.player.sendActionBar(text("Grave is currently opened by another player", arrayOf(199, 4, 30))); return}

        val owner = interaction.getTag<UUID>("owner")!!

        // Steal from grave
        if (e.player.uniqueId != owner && e.player.uniqueId == interaction.getTag<UUID>("killer")) {
            val lootedRemaining = interaction.getTag<Int>("looted") ?: 0
            if (lootedRemaining <= 0) {
                e.player.sendMessage(text("You have already looted this grave.", Utils.FAILED_COLOR))
                e.player.playSound(e.player, Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F)
                e.isCancelled = true
                return
            }

            interaction.setTag("looted", lootedRemaining - 1)
            val items: MutableList<ItemStack> = interaction.getListTag<ItemStack>("graveitems")!!.toMutableList()
            val possibleSteals = getPossibleSteals(items)

            if (possibleSteals.isEmpty()) {
                e.player.sendMessage(text("No possible items to steal.", Utils.FAILED_COLOR))
                e.player.playSound(e.player, Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F)
                e.isCancelled = true
                return
            }

            val steal = possibleSteals.random()
            items.remove(steal.clone())
            interaction.setListTag("graveitems", items)

            e.player.sendMessage(text("Item stolen.", Utils.SUCCESS_COLOR))
            Bukkit.getPlayer(owner)?.sendMessage(text("A(n) ${
                (steal.displayName() as TextComponent).content()
            } has been stolen from your grave.", Utils.FAILED_COLOR))

            CustomEffects.playSound(interaction.location, Sound.BLOCK_CHEST_CLOSE, 1.0F, 1.2F)
            e.player.addItemorClaim(steal)
            return
        }

        // Return, now we know the player opening this is the owner
        if (e.player.uniqueId != owner) return

        if (e.player.isSneaking) {
            val items: MutableList<ItemStack> = interaction.getListTag<ItemStack>("graveitems")!!.toMutableList()
            attemptAddItems(e.player, items)
            items.removeIf { it.type == Material.AIR }

            if (items.isEmpty()) {
                for (entity in interaction.location.subtract(0.5, 0.0, 0.5).getNearbyEntities(0.1, 0.1, 0.1))  {
                    if (entity.type == EntityType.BLOCK_DISPLAY) {entity.remove(); break}
                }
                for (entity in interaction.location.add(0.0,1.2, 0.0).getNearbyEntities(0.1, 0.1, 0.1)) {
                    if (entity.type == EntityType.TEXT_DISPLAY) {entity.remove(); break}
                }
                val uuid = interaction.getTag<UUID>("owner")!!
                val player: Player? = if (Bukkit.getServer().getPlayer(uuid) == null) Bukkit.getServer().getOfflinePlayer(uuid).player else Bukkit.getServer().getPlayer(uuid)
                if (player != null) {
                    val graves = player.getListTag<Location>("gravelist")!!
                    for (i in graves.indices.reversed()) {
                        if (interaction.world == graves[i].world && interaction.location.clone().subtract(Vector(0.5, 0.0, 0.5)).subtract(graves[i]).length() < 0.5) {
                            graves.removeAt(i)
                            break
                        }
                    }
                    player.removeTag("gravelist")
                    player.setListTag("gravelist", graves)
                }
                interaction.remove()
            }
            interaction.setListTag("graveitems", items)
            CustomEffects.playSound(interaction.location, Sound.BLOCK_CHEST_CLOSE, 1.0F, 1.2F)
        } else {
            GraveItemsGui(interaction).open(e.player)
        }
    }

    fun getPossibleSteals(items: List<ItemStack>): List<ItemStack> {
        val possibleSteals = mutableListOf<ItemStack>()
        // Is an artifact/rare item
        for (item in items) {
            if (item.getCustom()?.isArtifact == true)
                possibleSteals.add(item)
        }
        if (possibleSteals.isNotEmpty()) return possibleSteals
        // Is a custom (non-stackable), or has an overmax enchant
        for (item in items) {
            if (item.getCustom() != null && item.getCustom()?.stackable == false)
                possibleSteals.add(item)
            if (item.enchantments.keys.any { item.getEnchantmentLevel(it) > it.maxLevel })
                possibleSteals.add(item)
        }
        if (possibleSteals.isNotEmpty()) return possibleSteals
        // Is a max-level (3 enchs) enchanted item, or is a stackable custom
        for (item in items) {
            if (item.enchantments.keys.count { item.getEnchantmentLevel(it) == it.maxLevel } >= 3)
                possibleSteals.add(item)
            if (item.getCustom() != null && item.getCustom()?.stackable == true)
                possibleSteals.add(item)
        }
        if (possibleSteals.isNotEmpty()) return possibleSteals
        // Is an item with durability
        for (item in items) {
            if (item.hasData(DataComponentTypes.MAX_DAMAGE))
                possibleSteals.add(item)
        }
        return possibleSteals
    }
    private fun attemptAddItems(player: Player, items: MutableList<ItemStack>) {
        // Start with attempting to add all items that have a graveslot
        items.toList().filter { it.getTag<Int>("graveslot") != null }.forEach {
            val slot = getSlot(it.getTag<Int>("graveslot"))
            if (player.equipment.getItem(slot).type != Material.AIR) return@forEach
            player.equipment.setItem(slot, it)
            items.remove(it)
            it.removeTag("graveslot")
        }
        // Then fill the player's inventory with the rest of the items
        val newItems = items.toList()
        newItems.forEach {
            if (player.inventory.firstEmpty() == -1) return@forEach
            player.inventory.addItem(it)
            items.remove(it)
            it.removeTag("graveslot")
        }
        // Now the items left in items are only the ones that were unable to be added.
    }
    private fun getSlot(graveSlot: Int?): EquipmentSlot {
        return when (graveSlot) {
            0 -> EquipmentSlot.HEAD
            1 -> EquipmentSlot.CHEST
            2 -> EquipmentSlot.LEGS
            3 -> EquipmentSlot.FEET
            4 -> EquipmentSlot.OFF_HAND
            else -> EquipmentSlot.OFF_HAND
        }
    }

}