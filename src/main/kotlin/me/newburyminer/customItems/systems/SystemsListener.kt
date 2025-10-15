package me.newburyminer.customItems.systems

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.event.player.AsyncChatEvent
import io.papermc.paper.event.player.PlayerItemGroupCooldownEvent
import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.afkTime
import me.newburyminer.customItems.Utils.Companion.compassCooldown
import me.newburyminer.customItems.Utils.Companion.decrementTag
import me.newburyminer.customItems.Utils.Companion.getCustom
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.incrementTag
import me.newburyminer.customItems.Utils.Companion.isAfk
import me.newburyminer.customItems.Utils.Companion.isBeingTracked
import me.newburyminer.customItems.Utils.Companion.isInCombat
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.isTracking
import me.newburyminer.customItems.Utils.Companion.lore
import me.newburyminer.customItems.Utils.Companion.loreBlock
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setListTag
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.gui.GuiInventory
import me.newburyminer.customItems.gui.ShulkerHolder
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomEnchantments
import me.newburyminer.customItems.items.CustomItem
import net.kyori.adventure.text.TextComponent
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityToggleGlideEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.CompassMeta
import org.bukkit.scheduler.BukkitTask
import org.bukkit.util.Vector
import java.util.*
import kotlin.math.pow

class SystemsListener: Listener, Runnable  {
    @EventHandler fun onPlayerTeleport(e: PlayerTeleportEvent) {
        if (e.cause == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            if (e.player.getTag<Int>("deathcountdown") in arrayOf(0, null)) return
            e.isCancelled = true
            e.player.playSound(e.player, Sound.ENTITY_VILLAGER_NO, 1F, 1F)
        } else if (e.cause == PlayerTeleportEvent.TeleportCause.ENDER_PEARL && e.player.world != Bukkit.getWorlds()[2]) {
            if (e.player.getTag<Int>("deathcountdown") in arrayOf(0, null)) return
            e.isCancelled = true
            e.player.playSound(e.player, Sound.ENTITY_VILLAGER_NO, 1F, 1F)
        }
    }
    @EventHandler fun onPlayerElytra(e: EntityToggleGlideEvent) {
        if (e.entity !is Player) return
        if (!(e.entity as Player).isBeingTracked()) return
        Bukkit.getScheduler().runTaskLater(CustomItems.plugin, Runnable {
            (e.entity as Player).isGliding = false
        }, 1)
    }
    @EventHandler fun onPlayerChat(e: AsyncChatEvent) {
        if (e.player.getTag<Boolean>("lookingforname") != true) return
        e.isCancelled = true
        val toTrack = Bukkit.getPlayer((e.message() as TextComponent).content())
        if (toTrack == null) {
            e.player.sendMessage(Utils.text("Invalid name. Please try again.", Utils.FAILED_COLOR))
            return
        }
        if (toTrack.world == CustomItems.bossWorld) {
            e.player.sendMessage(
                Utils.text(
                    "That player is currently in a boss. Please wait a few minutes and try again.",
                    Utils.FAILED_COLOR
                )
            )
            return
        }
        if (toTrack.isAfk()) {
            e.player.sendMessage(
                Utils.text(
                    "That player is currently AFK. Please pick a different player and try again.",
                    Utils.FAILED_COLOR
                )
            )
            return
        }
        e.player.playSound(e.player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F)
        e.player.sendMessage(Utils.text("Tracking will begin in 1 minute on ${toTrack.name}.", Utils.SUCCESS_COLOR))
        e.player.setTag("lookingforname", false)

        toTrack.sendMessage(Utils.text("A player has started tracking you.", Utils.FAILED_COLOR, bold = true))
        toTrack.sendMessage(Utils.text("Tracking will begin in one minute.", Utils.FAILED_COLOR, bold = true))
        toTrack.sendMessage(
            Utils.text(
                "Logging out will give them your location and drop a random gear item of yours on the ground.",
                Utils.FAILED_COLOR,
                bold = true
            )
        )
        toTrack.sendMessage(Utils.text("You cannot use an ender chest or elytra.", Utils.FAILED_COLOR, bold = true))
        toTrack.sendMessage(
            Utils.text(
                "The tracking will last for 30 minutes after it begins.",
                Utils.FAILED_COLOR,
                bold = true
            )
        )
        if (toTrack.isBeingTracked()) toTrack.sendMessage(
            Utils.text(
                "Whoever was previously tracking you can still see your location.",
                Utils.FAILED_COLOR,
                bold = true
            )
        )
        toTrack.playSound(toTrack, Sound.ENTITY_WITHER_DEATH, 2.0F, 0.8F)
        // 31 minutes * 60 seconds * 1000 ms
        toTrack.setTag("compassend", System.currentTimeMillis() + 31 * 60 * 1000)
        e.player.setTag("compasscooldown", System.currentTimeMillis() + 31 * 60 * 1000)
        e.player.setTag("trackingplayer", toTrack.uniqueId)
        e.player.setTag("compassuses", (e.player.getTag<Int>("compassuses") ?: 0) + 1)

        futures.add(Bukkit.getScheduler().runTaskLater(CustomItems.plugin, Runnable {
            toTrack.sendMessage(
                Utils.text(
                    "The player tracking you can now see your location.",
                    Utils.FAILED_COLOR,
                    bold = true
                )
            )

            e.player.sendMessage(
                Utils.text(
                    "Tracking has begun. Right click your compass to update it.",
                    Utils.SUCCESS_COLOR
                )
            )
        }, 1200L).taskId)
    }
    @EventHandler fun onPlayerInteract(e: PlayerInteractEvent) {
        //blockEnderChest(e)
        compassStuff(e)
    }
    /*private fun blockEnderChest(e: PlayerInteractEvent) {
        if (e.clickedBlock?.type != Material.ENDER_CHEST) return
        if (!e.player.isBeingTracked()) return
        e.isCancelled = true
    }*/
    private fun compassStuff(e: PlayerInteractEvent) {
        if (!e.player.inventory.itemInMainHand.isItem(CustomItem.TRACKING_COMPASS)) return
        if (e.player.isTracking() && e.player.compassCooldown() < 30 * 60 * 20) {
            val player = Bukkit.getPlayer(e.player.getTag<UUID>("trackingplayer")!!)
            var loc = player?.location
            var name = player?.name
            if (player == null) {
                val offlinePlayer = Bukkit.getOfflinePlayer(e.player.getTag<UUID>("trackingplayer")!!)
                loc = offlinePlayer.location
                name = offlinePlayer.name
            }
            val newMeta = e.player.inventory.itemInMainHand.itemMeta as CompassMeta
            newMeta.lodestone = loc!!
            newMeta.isLodestoneTracked = false
            e.player.inventory.itemInMainHand.itemMeta = newMeta
            val worldName = when (loc.world) {
                Bukkit.getWorlds()[0] -> "overworld"
                Bukkit.getWorlds()[1] -> "nether"
                Bukkit.getWorlds()[2] -> "end"
                CustomItems.aridWorld -> "arid lands"
                else -> "unknown"
            }
            e.player.sendMessage(Utils.text("$name is currently in the $worldName.", Utils.SUCCESS_COLOR))
        } else {
            val inventory = GuiInventory("compass").inventory
            inventory.contents = GuiInventory.compassInv
            val base = GuiInventory.compassCost()
            val costs = getCost(e.player).toMutableList()
            base.setListTag("costs", costs)
            base.lore(
                Utils.text("Cost: ", arrayOf(222, 138, 53)),
                Utils.text("${costs[0]} Raw Iron Blocks", arrayOf(156, 146, 135)),
                Utils.text("${costs[1]} Raw Gold Blocks", arrayOf(222, 194, 53)),
                Utils.text("${costs[2]} Diamonds", arrayOf(92, 237, 225)),
                Utils.text("${costs[3]} Ancient Debris", arrayOf(71, 54, 40)),
                Utils.text("${costs[4]} Totem(s) of Undying", arrayOf(247, 207, 5)),
            )
            // 10 x times - 1
            inventory.setItem(22, base)
            e.player.openInventory(inventory)
        }
    }
    private fun getCost(player: Player): Array<Int> {
        val uses = (player.getTag<Int>("compassuses") ?: 0) + 1
        val rawIronBlocks = (6 * uses.toDouble().pow(0.3)).toInt()
        val rawGoldBlocks = (2 * uses.toDouble().pow(0.3)).toInt()
        val diamonds = (10 * uses.toDouble().pow(0.3)).toInt()
        val netherite = uses.toDouble().pow(0.6).toInt()
        val totems = (uses.toDouble().pow(0.5)).toInt()
        return arrayOf(rawIronBlocks, rawGoldBlocks, diamonds, netherite, totems)
    }
    @EventHandler fun entityDamageByEntity(e: EntityDamageEvent) {
        if (e.entity !is Player) return
        val player = e.entity as Player
        val selfInflicted = e.damageSource.causingEntity == player
        if (player.isAfk()) {
            if (player.afkTime() > 6000 || e.damageSource.causingEntity !is Player || selfInflicted) {
                e.isCancelled = true
                return
            }
            val hitter = e.damageSource.causingEntity as Player
            putInCombat(player)
            putInCombat(hitter)

            if (!player.isInCombat()) player.playSound(player, Sound.ITEM_SHIELD_BLOCK, 1.0F, 1.0F)

        } else {
            if (e.damageSource.causingEntity !is Player || selfInflicted) return
            val hitter = e.damageSource.causingEntity as Player
            putInCombat(player)
            putInCombat(hitter)
        }
    }
    @EventHandler fun onPlayerLogout(e: PlayerQuitEvent) {
        killInCombatLogout(e)
        dropCompassItem(e)
    }
    private fun killInCombatLogout(e: PlayerQuitEvent) {
        if (e.reason != PlayerQuitEvent.QuitReason.DISCONNECTED) return
        e.player.closeInventory()
        if (e.player.isInCombat()) {
            e.player.health = 0.0
        }
        e.player.setTag("combattime", 0)
    }
    private fun dropCompassItem(e: PlayerQuitEvent) {
        if (!e.player.isBeingTracked()) return
        val dropLoc = e.player.location

        val inventory = e.player.inventory.contents.toMutableList()
        inventory.removeIf { it == null }
        val items: MutableList<ItemStack> = inventory as MutableList<ItemStack>
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

        val steal = possibleSteals.randomOrNull() ?: if (e.player.health == 0.0) ItemStack(Material.AIR) else ItemStack(Material.PAPER).loreBlock(
            Utils.text(
                "If you have this item and the player you were tracking did not combat log, the player you were tracking likely dumped all of their items into a chest. Please contact NewburyMiner so that they can retrive an item from the player you tracked.",
                Utils.GRAY
            )
        )
        dropLoc.world.spawn(dropLoc, Item::class.java) {
            it.itemStack = steal.clone()
            it.isGlowing = true
            it.isUnlimitedLifetime = true
            it.isInvulnerable = true
            it.location.y += 0.1
            if (it.location.y <= it.world.minHeight + 4) {
                it.location.y = it.world.maxHeight.toDouble()
            }
            it.setGravity(false)
            it.setNoPhysics(true)
            it.velocity = Vector(0, 0, 0)
        }
        e.player.inventory.removeItemAnySlot(steal.clone())
    }

    @EventHandler fun onPlayerDeath(e: PlayerDeathEvent) {
        e.player.setTag("deathcountdown", 300)
        Bukkit.getScheduler().runTaskLater(CustomItems.plugin, Runnable {
            e.player.setTag("combattime", 0)
        }, 10L)
    }
    private fun putInCombat(player: Player) {
        Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
            if (player.isAfk()) {
                player.setTag("isafk", false)
                player.setTag("afktime", 0)
            }
            if (!player.isInCombat()) {
                player.playSound(player, Sound.ITEM_SHIELD_BLOCK, 1.0F, 1.0F)
                player.sendMessage(
                    Utils.text(
                        "You are in combat. Do not logout or you will be killed.",
                        Utils.FAILED_COLOR
                    )
                )
            }
            player.setTag("combattime", 1200)
            player.setTag("combattimestamp", System.currentTimeMillis())
        })
    }
    @EventHandler fun onPlayerClickInv(e: InventoryClickEvent) {
        if (e.whoClicked !is Player) return
        if (!(e.whoClicked as Player).isBeingTracked()) return
        if (e.inventory.type != InventoryType.ENDER_CHEST) return
        if (e.action in arrayOf(
                InventoryAction.HOTBAR_SWAP, InventoryAction.SWAP_WITH_CURSOR
            )) {
            e.isCancelled = true
            return
        }
        if (e.action in arrayOf(
            InventoryAction.PLACE_ALL, InventoryAction.PLACE_SOME, InventoryAction.PLACE_ONE, InventoryAction.PLACE_FROM_BUNDLE,
            InventoryAction.PLACE_SOME_INTO_BUNDLE, InventoryAction.PLACE_ALL_INTO_BUNDLE,
        ) && e.clickedInventory == e.inventory) {
            e.isCancelled = true
            return
        }
        if (e.action in arrayOf(
                InventoryAction.MOVE_TO_OTHER_INVENTORY
        ) && e.clickedInventory != e.inventory) {
            e.isCancelled = true
            return
        }
        return
    }

    @EventHandler fun onPlayerJoin(e: PlayerJoinEvent) {
        e.player.getAttribute(Attribute.MAX_ABSORPTION)!!.baseValue = 2048.0
    }
    @EventHandler fun onCooldownSet(e: PlayerItemGroupCooldownEvent) {
        if (e.cooldownGroup.namespace != "customitems") return
        if (e.cooldown != 1) return
        e.isCancelled = true
    }
    @EventHandler fun onInventoryClick(e: InventoryClickEvent) {
        if (e.action != InventoryAction.PICKUP_HALF) return
        if (e.whoClicked.getTag<Boolean>("inventoryshulker") != true) return
        if (e.clickedInventory?.type !in
            arrayOf(InventoryType.ENDER_CHEST, InventoryType.PLAYER)
        ) return
        if (!Tag.SHULKER_BOXES.isTagged(e.clickedInventory!!.getItem(e.slot)?.type ?: Material.AIR)) return
        e.isCancelled = true
        val shulker = e.clickedInventory!!.getItem(e.slot)!!
        if (shulker.getTag<Boolean>("shulkeropen") == true) {
            CustomEffects.playSound(e.whoClicked.location, Sound.ENTITY_SHULKER_HURT, 1.0F, 1.2F)
            return
        }
        CustomEffects.playSound(e.whoClicked.location, Sound.BLOCK_SHULKER_BOX_OPEN, 1.0F, 1.0F)
        val player = e.whoClicked as Player
        Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
            shulker.setTag("shulkeropen", true)
            player.closeInventory()
            player.openInventory(ShulkerHolder(shulker).inventory)
        })
    }
    @EventHandler fun onItemCraft(e: CraftItemEvent) {
        cancelCustomCrafts(e)
        duplicateArmorTrims(e)
    }
    private fun cancelCustomCrafts(e: CraftItemEvent) {
        for (item in e.inventory) {
            if (item == null) continue
            if (item.itemMeta == null) continue
            if (item.getTag<Int>("id") != null) e.isCancelled = true
        }
    }
    private fun duplicateArmorTrims(e: CraftItemEvent) {
        val result = e.recipe.result
        if (result.type !in arrayOf(
                Material.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE, Material.VEX_ARMOR_TRIM_SMITHING_TEMPLATE, Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE, Material.COAST_ARMOR_TRIM_SMITHING_TEMPLATE,
                Material.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE, Material.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE, Material.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE, Material.HOST_ARMOR_TRIM_SMITHING_TEMPLATE,
                Material.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE, Material.WARD_ARMOR_TRIM_SMITHING_TEMPLATE, Material.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, Material.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE,
                Material.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE, Material.RIB_ARMOR_TRIM_SMITHING_TEMPLATE, Material.EYE_ARMOR_TRIM_SMITHING_TEMPLATE, Material.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE,
                Material.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE, Material.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE, Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE
            )) return
        if (e.inventory.getItem(2)!!.enchantments[CustomEnchantments.DUPLICATE] == 1) {
            e.whoClicked.sendMessage(text("You cannot use duplicated trims in this recipe.", Utils.FAILED_COLOR))
            (e.whoClicked as Player).playSound(e.whoClicked, Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F)
            e.isCancelled = true
            return
        }
        if (e.isShiftClick) {
            e.isCancelled = true
            return
        }
        Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
            if (e.inventory.getItem(2)?.type != result.type) {
                val newResult = ItemStack(result.type)
                e.inventory.setItem(2, newResult)
            } else {
                e.inventory.getItem(2)!!.amount = 2
            }
        })
    }
    @EventHandler fun onInteract(e: PlayerInteractEvent) {
        cancelProjectileCharge(e)
    }
    private fun cancelProjectileCharge(e: PlayerInteractEvent) {
        if (e.action != Action.RIGHT_CLICK_BLOCK && e.action != Action.RIGHT_CLICK_AIR) return
        if (e.item == null) return
        if (e.item!!.type != Material.BOW && e.item!!.type != Material.CROSSBOW) return
        for (custom in arrayOf(CustomItem.WIND_HOOK)) {
            if (e.item!!.isItem(custom) && !e.item!!.offCooldown(e.player)) e.isCancelled = true
        }
    }

    private var futures = mutableListOf<Int>()
    private var removalItems: MutableMap<UUID, MutableList<ItemStack>> = mutableMapOf()

    private var counter: Int = 0
    private lateinit var mainFuture: BukkitTask
    override fun run() {
        mainFuture = Bukkit.getScheduler().runTaskTimer(CustomItems.plugin, Runnable {
            counter = if (counter == 2400) 0 else counter + 1
            if (counter % 20 == 0) {
                for (player in Bukkit.getServer().onlinePlayers) {
                    player.decrementTag("deathcountdown")
                }
            }
            for (player in Bukkit.getServer().onlinePlayers) {
                if (player.isAfk()) {
                    val tpSpot = player.getTag<Location>("afklocation")!!
                    if (player.x != tpSpot.x || player.y != tpSpot.y || player.z != tpSpot.z || player.world != tpSpot.world) {
                        player.teleport(tpSpot)
                    }
                    player.incrementTag("afktime")
                }
                if (player.isInCombat()) {
                    player.decrementTag("combattime")
                    if (!player.isInCombat()) player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2.0F, 1.0F)
                }
            }
        }, 0L, 1L)
    }

    fun cancel() {
        mainFuture.cancel()
        for (future in futures) {
            Bukkit.getScheduler().cancelTask(future)
        }
    }

}