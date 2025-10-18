package me.newburyminer.customItems.systems

import io.papermc.paper.datacomponent.DataComponentTypes
import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.addItemorDrop
import me.newburyminer.customItems.Utils.Companion.getCustom
import me.newburyminer.customItems.Utils.Companion.getListTag
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.removeTag
import me.newburyminer.customItems.Utils.Companion.serializeAsBytes
import me.newburyminer.customItems.Utils.Companion.setListTag
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.entities.CustomEntity
import me.newburyminer.customItems.gui.GraveItemsGui
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomEnchantments
import org.bukkit.*
import org.bukkit.damage.DamageType
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerInteractAtEntityEvent
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

class GraveListener: Listener {
    @EventHandler
    fun onPlayerDeath(e: PlayerDeathEvent) {
        createGrave(e)
    }
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
        if (e.player.world != Bukkit.getServer().worlds[0] && e.player.world != Bukkit.getServer().worlds[1]
            && e.player.world != Bukkit.getServer().worlds[2] && e.player.world != CustomItems.aridWorld) return
        if (e.isCancelled) return
        if ((e.player.getTag<Int>("experiencekept") ?: 0) != 0) {
            val extraExp = e.player.exp * (Utils.toExpAmount(e.player.level + 1) - Utils.toExpAmount(e.player.level))
            val totalExp = (Utils.toExpAmount(e.player.level, extraExp.toInt())) * (e.player.getTag<Int>("experiencekept")!! / 4.0)
            val newAmounts = Utils.toExpLevel(totalExp.toInt())
            e.newLevel = newAmounts.first
            e.newExp = newAmounts.second
            e.newTotalExp = totalExp.toInt()
            e.setShouldDropExperience(false)
        }
        if (e.drops.size == 0) return
        // finding location
        val loc = e.player.location
        loc.y = if (e.player.world == Bukkit.getServer().worlds[0] && loc.y < -64.0) -64.0 else loc.y
        loc.y = if (e.player.world != Bukkit.getServer().worlds[0] && loc.y < 0.0) 0.0 else loc.y
        if (loc.world.getBlockAt(loc).type != Material.AIR && loc.world.getBlockAt(loc).type != Material.WATER) {
            while (loc.world.getBlockAt(loc).type != Material.AIR && loc.world.getBlockAt(loc).type != Material.WATER) {
                loc.y += 1
                if (loc.y >= 255) {
                    e.keepInventory = true
                    e.drops.clear()
                    return
                }
            }
        } else {
            while (loc.world.getBlockAt(loc).type == Material.AIR || loc.world.getBlockAt(loc).type == Material.WATER) {
                loc.y -= 1
            }
            loc.y += 1
        }
        val graveList = (e.player.getListTag<Location>("gravelist") ?: listOf()).toMutableList()
        graveList.add(loc.world.getBlockAt(loc).location)
        e.player.setListTag("gravelist", graveList)
        val chestDisplay: BlockDisplay = e.player.world.spawn(loc.world.getBlockAt(loc).location, BlockDisplay::class.java)
        val armorStand: Interaction = e.player.world.spawn(loc.world.getBlockAt(loc).location.add(0.5, 0.0, 0.5), Interaction::class.java) {
            it.interactionHeight = 1.0F
            it.interactionWidth = 1.0F
        }
        val textDisplay: TextDisplay = e.player.world.spawn(loc.world.getBlockAt(loc).location.add(0.5, 1.2, 0.5), TextDisplay::class.java)
        // remove anything not to be dropped here
        var helmet: ItemStack? = null
        var chestplate: ItemStack? = null
        var leggings: ItemStack? = null
        var boots: ItemStack? = null
        var offhand: ItemStack? = null
        val drops = e.drops.toMutableList()
        if (e.player.inventory.itemInOffHand.type != Material.AIR) offhand = drops.removeLast()
        for (item in drops) {
            if (CustomEnchantments.SOULBOUND in item.enchantments) {e.itemsToKeep.add(item); continue}
            if (Tag.ITEMS_HEAD_ARMOR.isTagged(item.type)) helmet = item
            if (Tag.ITEMS_CHEST_ARMOR.isTagged(item.type)) chestplate = item
            if (Tag.ITEMS_LEG_ARMOR.isTagged(item.type)) leggings = item
            if (Tag.ITEMS_FOOT_ARMOR.isTagged(item.type)) boots = item
        }
        helmet?.setTag("graveslot", 0)
        chestplate?.setTag("graveslot", 1)
        leggings?.setTag("graveslot", 2)
        boots?.setTag("graveslot", 3)
        offhand?.setTag("graveslot", 4)
        for (item in e.itemsToKeep) {
            e.drops.remove(item)
        }
        armorStand.setListTag("graveitems", e.drops.toMutableList())
        // can change this later to for loop and skip anything
        saveGrave(e, e.drops.toMutableList())
        e.drops.clear()
        chestDisplay.isInvulnerable = true
        chestDisplay.block = Material.CHEST.createBlockData()
        chestDisplay.displayWidth = 1F
        chestDisplay.displayHeight = 1F
        chestDisplay.setGravity(false)
        chestDisplay.setNoPhysics(true)
        armorStand.isInvulnerable = true
        armorStand.isInvisible = true
        armorStand.setGravity(false)
        armorStand.setNoPhysics(true)
        //armorStand.customName(text("${e.player.name}'s grave", arrayOf(199, 4, 30)))
        //armorStand.isCustomNameVisible = false
        armorStand.setTag("id", CustomEntity.GRAVE_MARKER.id)
        armorStand.setTag("currentlyopen", false)
        armorStand.setTag("owner", e.player.uniqueId)
        if (e.damageSource.causingEntity is Player) {
            armorStand.setTag("killer", (e.damageSource.causingEntity as Player).uniqueId)
            armorStand.setTag("looted", false)
        } else if (e.damageSource.damageType == DamageType.GENERIC && e.player.lastDamageCause!!.damageSource.causingEntity is Player) {
            armorStand.setTag("killer", (e.player.lastDamageCause!!.damageSource.causingEntity as Player).uniqueId)
            armorStand.setTag("looted", false)
        }
        e.player.setTag("gravetpcooldown", (5 * 60 * 1000 + System.currentTimeMillis()))
        textDisplay.billboard = Display.Billboard.CENTER
        textDisplay.text(text("${e.player.name}'s grave", arrayOf(199, 4, 30)))
    }
    private fun saveGrave(e: PlayerDeathEvent, drops: MutableList<ItemStack>) {
        val fileName = System.getProperty("user.dir") + "/plugins/customItems/savedGraves.txt"
        val file = File("plugins/customItems/savedGraves.txt")
        if (!file.exists()) {
            file.createNewFile()
        }
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

    @EventHandler
    fun onPlayerInteractThing(e: PlayerInteractAtEntityEvent) {
        openGrave(e)
    }
    private fun openGrave(e: PlayerInteractAtEntityEvent) {
        e.player.activeBossBars()
        if (e.rightClicked !is Interaction) return
        if (e.rightClicked.getTag<Int>("id") != CustomEntity.GRAVE_MARKER.id) return
        val armorStand: Interaction = e.rightClicked as Interaction
        if (armorStand.getTag<Boolean>("currentlyopen") == true) {e.player.sendActionBar(text("Grave is currently opened by another player", arrayOf(199, 4, 30))); return}
        val owner = armorStand.getTag<UUID>("owner")!!
        if (e.player.uniqueId != owner) {
            if (e.player.uniqueId == armorStand.getTag<UUID>("killer")) {
                if (armorStand.getTag<Boolean>("looted") == true) {
                    e.player.sendMessage(text("You have already looted this grave.", Utils.FAILED_COLOR))
                    e.player.playSound(e.player, Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F)
                    e.isCancelled = true
                    return
                }
                val items: MutableList<ItemStack> = armorStand.getListTag<ItemStack>("graveitems")!!.toMutableList()
                val possibleSteals = mutableListOf<ItemStack>()
                for (item in items) {
                    if (item.getCustom() != null) {
                        possibleSteals.add(item)
                    }
                    var overMax = false
                    var totalMax = 0
                    for (enchantment in item.enchantments.keys) {
                        if (item.enchantments[enchantment]!! > enchantment.maxLevel) {
                            overMax = true
                            totalMax++
                        } else if (item.enchantments[enchantment]!! == enchantment.maxLevel) {
                            totalMax++
                        }
                    }
                    if (totalMax > 2 || overMax) {
                        possibleSteals.add(item)
                    }
                }
                if (possibleSteals.isEmpty()) {
                    for (item in items) {
                        if (item.hasData(DataComponentTypes.MAX_DAMAGE)) {
                            possibleSteals.add(item)
                        }
                    }
                }
                armorStand.setTag("looted", true)
                if (possibleSteals.isEmpty()) {
                    e.player.sendMessage(text("No possible items to steal.", Utils.FAILED_COLOR))
                    e.player.playSound(e.player, Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F)
                    e.isCancelled = true
                    return
                }
                val steal = possibleSteals.random()
                items.remove(steal.clone())
                armorStand.setListTag("graveitems", items)
                e.player.sendMessage(text("Item stolen.", Utils.SUCCESS_COLOR))
                CustomEffects.playSound(armorStand.location, Sound.BLOCK_CHEST_CLOSE, 1.0F, 1.2F)
                e.player.addItemorDrop(steal)
            }
            return
        }
        if (e.player.isSneaking) {
            val items: MutableList<ItemStack> = armorStand.getListTag<ItemStack>("graveitems")!!.toMutableList()
            val toRemove = mutableListOf<ItemStack>()
            val toAdd = mutableListOf<ItemStack>()

            for (item in items) {
                if (item.getTag<Int>("graveslot") != null) {
                    var extraItem: ItemStack? = null
                    when (item.getTag<Int>("graveslot")) {
                        0 -> {if (e.player.inventory.helmet != null) extraItem = e.player.inventory.helmet!!.clone(); e.player.inventory.helmet = item}
                        1 -> {if (e.player.inventory.chestplate != null) extraItem = e.player.inventory.chestplate!!.clone(); e.player.inventory.chestplate = item}
                        2 -> {if (e.player.inventory.leggings != null) extraItem = e.player.inventory.leggings!!.clone(); e.player.inventory.leggings = item}
                        3 -> {if (e.player.inventory.boots != null) extraItem = e.player.inventory.boots!!.clone(); e.player.inventory.boots = item}
                        4 -> {if (e.player.inventory.itemInOffHand.type != Material.AIR) extraItem = e.player.inventory.itemInOffHand.clone(); e.player.inventory.setItemInOffHand(item)}
                    }
                    if (extraItem != null) {
                        if (e.player.inventory.firstEmpty() == -1) {
                            toAdd.add(extraItem.clone())
                            item.removeTag("graveslot")
                            toRemove.add(item)
                            break
                        }
                        e.player.inventory.addItem(extraItem)
                    }
                    item.removeTag("graveslot")
                    toRemove.add(item)
                } else {
                    if (e.player.inventory.firstEmpty() == -1) break
                    toRemove.add(item.clone())
                    e.player.inventory.addItem(item.clone())
                }
            }
            for (item in toRemove) {
                items.remove(item)
            }
            for (item in toAdd) {
                items.add(item)
            }
            items.removeIf { it.type == Material.AIR }
            if (items.size == 0) {
                for (entity in armorStand.location.subtract(0.5, 0.0, 0.5).getNearbyEntities(0.1, 0.1, 0.1))  {
                    if (entity.type == EntityType.BLOCK_DISPLAY) {entity.remove(); break}
                }
                for (entity in armorStand.location.add(0.0,1.2, 0.0).getNearbyEntities(0.1, 0.1, 0.1)) {
                    if (entity.type == EntityType.TEXT_DISPLAY) {entity.remove(); break}
                }
                val uuid = armorStand.getTag<UUID>("owner")!!
                val player: Player? = if (Bukkit.getServer().getPlayer(uuid) == null) Bukkit.getServer().getOfflinePlayer(uuid).player else Bukkit.getServer().getPlayer(uuid)
                if (player != null) {
                    val graves = player.getListTag<Location>("gravelist")!!
                    for (i in graves.indices.reversed()) {
                        if (armorStand.world == graves[i].world && armorStand.location.clone().subtract(Vector(0.5, 0.0, 0.5)).subtract(graves[i]).length() < 0.5) {
                            graves.removeAt(i)
                            break
                        }
                    }
                    player.removeTag("gravelist")
                    player.setListTag("gravelist", graves)
                }
                armorStand.remove()
            }
            armorStand.setListTag("graveitems", items)
            CustomEffects.playSound(armorStand.location, Sound.BLOCK_CHEST_CLOSE, 1.0F, 1.2F)
        } else {
            GraveItemsGui(armorStand).open(e.player)
        }
    }
}