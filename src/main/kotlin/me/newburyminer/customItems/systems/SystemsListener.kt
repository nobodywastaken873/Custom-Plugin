package me.newburyminer.customItems.systems

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.event.player.AsyncChatEvent
import io.papermc.paper.event.player.PlayerItemGroupCooldownEvent
import io.papermc.paper.event.player.PlayerTradeEvent
import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.afkTime
import me.newburyminer.customItems.Utils.Companion.decrementTag
import me.newburyminer.customItems.Utils.Companion.getCustom
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.incrementTag
import me.newburyminer.customItems.Utils.Companion.isAfk
import me.newburyminer.customItems.Utils.Companion.isBeingTracked
import me.newburyminer.customItems.Utils.Companion.isInCombat
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.loreBlock
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.gui.CustomGui
import me.newburyminer.customItems.gui.inventory.ShulkerGui
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomEnchantments
import me.newburyminer.customItems.items.CustomItem
import net.kyori.adventure.text.TextComponent
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.block.Crafter
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.block.CrafterCraftEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityToggleGlideEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.*
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.inventory.CrafterInventory
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitTask
import org.bukkit.util.Vector
import java.util.*

class SystemsListener: Listener, Runnable  {
    @EventHandler fun onPlayerTeleport(e: PlayerTeleportEvent) {
        if (e.cause == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            //if (e.player.getTag<Int>("deathcountdown") in arrayOf(0, null)) return
            //e.isCancelled = true
            //e.player.playSound(e.player, Sound.ENTITY_VILLAGER_NO, 1F, 1F)
        } /*else if (e.cause == PlayerTeleportEvent.TeleportCause.ENDER_PEARL && e.player.world != Bukkit.getWorlds()[2]) {
            if (e.player.getTag<Int>("deathcountdown") in arrayOf(0, null)) return
            e.isCancelled = true
            e.player.playSound(e.player, Sound.ENTITY_VILLAGER_NO, 1F, 1F)
        }*/
    }
    //@EventHandler fun onPlayerMove(e: PlayerMoveEvent) {
        //if (e.player.world.name == "world_the_end") e.player.health = 0.0
    //}
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

    @EventHandler fun entityDamageByEntity(e: EntityDamageEvent) {
        handleCombat(e)
        preventDamagingTrusted(e)
    }
    private fun handleCombat(e: EntityDamageEvent) {
        if (e.entity !is Player) return
        val player = e.entity as Player
        val selfInflicted = e.damageSource.causingEntity == player
        if (player.isAfk()) {
            if (player.afkTime() > 6000 || e.damageSource.causingEntity !is Player || selfInflicted) {
                e.isCancelled = true
                return
            }
            val hitter = e.damageSource.causingEntity as Player
            putInCombat(player, hitter)
            putInCombat(hitter, player)

            if (!player.isInCombat()) player.playSound(player, Sound.ITEM_SHIELD_BLOCK, 1.0F, 1.0F)

        } else {
            if (e.damageSource.causingEntity !is Player || selfInflicted) return
            val hitter = e.damageSource.causingEntity as Player
            putInCombat(player, hitter)
            putInCombat(hitter, player)
        }
    }
    private fun preventDamagingTrusted(e: EntityDamageEvent) {
        if (e.entity !is Player) return
        val player = e.entity as Player
        val selfInflicted = e.damageSource.causingEntity == player
        if (e.damageSource.causingEntity !is Player || selfInflicted) return
        val hitter = e.damageSource.causingEntity as Player

        if (!TrustSystem.trusts(player, hitter)) return
        if (e.entity.world == CustomItems.bossWorld || e.entity.world == CustomItems.aridWorld || hitter.isInCombat()) {
            e.isCancelled = true
        }
    }
    @EventHandler fun onPlayerLogout(e: PlayerQuitEvent) {
        killInCombatLogout(e)
        dropCompassItem(e)
        closeInventories(e)
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
        val items = inventory.filterNotNull()
        val possibleSteals = GraveListener.getPossibleSteals(items)

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
    private fun closeInventories(e: PlayerQuitEvent) {
        val openInventory = e.player.openInventory
        if (openInventory.topInventory.holder is CustomGui) {
            (openInventory.topInventory.holder as CustomGui).onClose(InventoryCloseEvent(openInventory))
        }
        e.player.closeInventory()
    }

    @EventHandler fun onPlayerDeath(e: PlayerDeathEvent) {
        e.player.setTag("deathcountdown", 300)
        Bukkit.getScheduler().runTaskLater(CustomItems.plugin, Runnable {
            e.player.setTag("combattime", 0)
        }, 10L)
    }
    private fun putInCombat(player: Player, damager: Player) {
        Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {

            if (TrustSystem.trusts(damager, player)) return@Runnable

            if (player.isAfk()) {
                player.setTag("isafk", false)
                player.setTag("afktime", 0)
            }
            if (!player.isInCombat()) {
                player.playSound(player, Sound.ITEM_SHIELD_BLOCK, 1.0F, 1.0F)
                player.sendMessage(
                    text(
                        "You are in combat. Do not logout or you will be killed.",
                        Utils.FAILED_COLOR
                    )
                )

                if (player.equipment.leggings.isItem(CustomItem.REACTIVE_CASING)) {
                    player.addPotionEffects(listOf(
                        PotionEffect(PotionEffectType.RESISTANCE, 200, 3),
                        PotionEffect(PotionEffectType.FIRE_RESISTANCE, 400, 0),
                        PotionEffect(PotionEffectType.REGENERATION, 400, 1),
                    ))
                }

                if (player.equipment.leggings.isItem(CustomItem.AUTO_INJECTING_LEGGINGS)) {
                    player.addPotionEffects(listOf(
                        PotionEffect(PotionEffectType.STRENGTH, 4800, 1),
                        PotionEffect(PotionEffectType.FIRE_RESISTANCE, 9600, 0),
                        PotionEffect(PotionEffectType.SPEED, 4800, 1),
                    ))
                }

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
        e.player.sendMessage(Utils.text("Learn more about what this server changes in /info!", Utils.GRAY))
    }
    @EventHandler fun onCooldownSet(e: PlayerItemGroupCooldownEvent) {
        if (e.cooldownGroup.namespace != "customitems") return
        if (e.cooldown != 1) return
        e.isCancelled = true
    }
    // Open shulkers in inventory
    @EventHandler fun onInventoryClick(e: InventoryClickEvent) {
        if (e.action != InventoryAction.PICKUP_HALF) return
        if (e.whoClicked.getTag<Boolean>("inventoryshulker") != true) return

        val clickedInventory = e.clickedInventory ?: return
        val clickedItem = clickedInventory.getItem(e.slot) ?: return

        if (clickedInventory.type !in arrayOf(InventoryType.ENDER_CHEST, InventoryType.PLAYER)) return
        if (!Tag.SHULKER_BOXES.isTagged(clickedItem.type)) return

        e.isCancelled = true
        if (clickedItem.getTag<Boolean>("shulkeropen") == true) {
            CustomEffects.playSoundToPlayer(e.whoClicked as Player, Sound.ENTITY_SHULKER_HURT, 1.0F, 1.2F)
            return
        }

        CustomEffects.playSoundToPlayer(e.whoClicked as Player, Sound.BLOCK_SHULKER_BOX_OPEN, 1.0F, 1.0F)
        val player = e.whoClicked as Player
        Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
            clickedItem.setTag("shulkeropen", true)
            player.closeInventory()
            ShulkerGui(clickedItem).open(player)
        })
    }
    private val trims = arrayOf(
        Material.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE, Material.VEX_ARMOR_TRIM_SMITHING_TEMPLATE, Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE, Material.COAST_ARMOR_TRIM_SMITHING_TEMPLATE,
        Material.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE, Material.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE, Material.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE, Material.HOST_ARMOR_TRIM_SMITHING_TEMPLATE,
        Material.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE, Material.WARD_ARMOR_TRIM_SMITHING_TEMPLATE, Material.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, Material.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE,
        Material.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE, Material.RIB_ARMOR_TRIM_SMITHING_TEMPLATE, Material.EYE_ARMOR_TRIM_SMITHING_TEMPLATE, Material.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE,
        Material.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE, Material.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE, Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE
    )
    @EventHandler fun onCrafterCraft(e: CrafterCraftEvent) {
        val crafter = e.block.state as Crafter
        val inv = crafter.inventory
        if (inv.any { (it != null) && (it.getCustom() != null || it.type in trims) }) {
            e.isCancelled = true
            return
        }
    }
    @EventHandler fun onItemCraft(e: CraftItemEvent) {
        cancelCustomCrafts(e)
        duplicateArmorTrims(e)
    }
    // Prevent crafting items in a crafting table with a custom
    private fun cancelCustomCrafts(e: CraftItemEvent) {
        for (item in e.inventory) {
            if (item == null) continue
            if (item.getCustom() != null) e.isCancelled = true
        }
    }
    // Unique trim duplicating
    private fun duplicateArmorTrims(e: CraftItemEvent) {
        val result = e.recipe.result
        if (e.isCancelled) return
        if (e.result == Event.Result.DENY || e.result == Event.Result.DEFAULT) return
        if (result.type !in trims) return
        if (e.inventory.getItem(2)!!.enchantments[CustomEnchantments.DUPLICATE] == 1) {
            e.whoClicked.sendMessage(text("You cannot use duplicated trims in this recipe.", Utils.FAILED_COLOR))
            (e.whoClicked as Player).playSound(e.whoClicked, Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F)
            e.isCancelled = true
            return
        }
        /*if (e.click != ClickType.LEFT) {
            e.isCancelled = true
            return
        }
        if (
            (e.cursor.type != e.inventory.getItem(2)!!.type || e.cursor.getEnchantmentLevel(CustomEnchantments.DUPLICATE) != 1 || !result.isSimilar(e.cursor)) &&
            e.cursor.type != Material.AIR
            ) {
            e.isCancelled = true
            return
        }*/
        if (e.isShiftClick) {
            e.isCancelled = true
            return
        }
        val currentCount = e.inventory.getItem(2)?.amount ?: 1
        val trimType = e.inventory.getItem(2)?.type ?: Material.COAST_ARMOR_TRIM_SMITHING_TEMPLATE
        Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
            // If it turns into air after crafting, put one back
            if (e.inventory.getItem(2)?.type != trimType) {
                val newResult = ItemStack(trimType)
                e.inventory.setItem(2, newResult)
            // Otherwise, increase the amount by 1
            } else {
                e.inventory.getItem(2)?.amount = currentCount
            }
        })
    }
    @EventHandler fun onInteract(e: PlayerInteractEvent) {
        cancelProjectileCharge(e)
        addUniqueSalt(e)
    }
    // Prevent charging if on cooldown, not really needed anymore since the item cooldown is there
    private fun cancelProjectileCharge(e: PlayerInteractEvent) {
        if (e.action != Action.RIGHT_CLICK_BLOCK && e.action != Action.RIGHT_CLICK_AIR) return
        if (e.item == null) return
        if (e.item!!.type != Material.BOW && e.item!!.type != Material.CROSSBOW) return
        for (custom in arrayOf(CustomItem.WIND_HOOK)) {
            if (e.item!!.isItem(custom) && !e.item!!.offCooldown(e.player)) e.isCancelled = true
        }
    }
    // Add a unique UUID tag to items, prevent them from being stackable, only applies to non-materials
    // Should occur when crafting, but added just in case
    private fun addUniqueSalt(e: PlayerInteractEvent) {
        if (e.item?.getCustom()?.stackable != false) return
        if (e.item?.getTag<String>("uniquesalt") != null) return
        e.item?.setTag("uniquesalt", UUID.randomUUID().toString())
    }
    // Prevent customs from being placed (need to add any placeable customs to this list)
    @EventHandler fun onBlockPlace(e: BlockPlaceEvent) {
        if (e.itemInHand.getTag<Int>("id") != null && e.itemInHand.getCustom() !in arrayOf(
                CustomItem.ACTUAL_REDSTONE, CustomItem.CONTAINERS, CustomItem.MINECART_MATERIALS, CustomItem.INPUT_DEVICES,
                CustomItem.POCKETKNIFE_MULTITOOL, CustomItem.TREECAPITATOR, CustomItem.NETHERITE_MULTITOOL, CustomItem.HOE,
                CustomItem.REDSTONE_AMALGAMATION, CustomItem.HEAVY_GREATHAMMER, CustomItem.GRAVITY_HAMMER
            )) {
            e.isCancelled = true
        }
    }
    // Prevent customs from being used in trades they aren't supposed to be in
    @EventHandler fun onVillagerTrade(e: PlayerTradeEvent) {
        if (
            e.player.openInventory.getItem(0)?.getCustom() == e.trade.ingredients.getOrNull(0)?.getCustom() &&
            e.player.openInventory.getItem(1)?.getCustom() == e.trade.ingredients.getOrNull(1)?.getCustom()
            ) return
        e.isCancelled = true
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