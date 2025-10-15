package me.newburyminer.customItems.items

import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent
import me.newburyminer.customItems.Utils.Companion.getCustom
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.offCooldown
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.*
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.*
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

class ItemEventHandler: Listener {
    companion object {
        private val behaviors: MutableMap<CustomItem, CustomItemBehavior> = mutableMapOf()

        fun register(customItem: CustomItem, behavior: CustomItemBehavior) {
            behaviors[customItem] = behavior
        }

        private fun dispatch(
            player: Player?,
            item: ItemStack?,
            event: Event,
            itemType: EventItemType = EventItemType.UNKNOWN,
            source: Entity? = null,
            target: Entity? = null
        ) {
            val custom = item?.getCustom() ?: return
            val behavior = behaviors[custom] ?: return
            behavior.handle(
                EventContext(
                    player = player,
                    item = item,
                    event = event,
                    itemType = itemType,
                    sourceEntity = source,
                    targetEntity = target
                )
            )
        }

        private fun handleArmor(player: Player, e: Event) {
            val inv = player.inventory
            val slots = mapOf(
                EventItemType.HELMET to inv.helmet,
                EventItemType.CHESTPLATE to inv.chestplate,
                EventItemType.LEGGINGS to inv.leggings,
                EventItemType.BOOTS to inv.boots
            )

            for ((slot, item) in slots) {
                if (item != null) dispatch(player, item, e, slot)
            }
        }
    }

    @EventHandler fun entityKnockbackEntity(e: EntityKnockbackByEntityEvent) {
        val player = e.hitBy as? Player
        if (player != null) {
            dispatch(player, player.inventory.itemInMainHand, e, EventItemType.MAINHAND, player, e.entity)
        }
    }

    @EventHandler fun playerEatItem(e: PlayerItemConsumeEvent) {
        val player = e.player
        dispatch(player, e.item, e, EventItemType.HAND)
    }

    @EventHandler fun inventoryClick(e: InventoryClickEvent) {
        val player = e.whoClicked as Player
        // Should check if click is outside of inventory, not on any slot
        if (e.slot < 0) return
        // Needs to be modified for anything other than clicking on a single item in inventory (ie offhand etc.)
        val item = e.clickedInventory?.getItem(e.slot)
        dispatch(player, item, e)
    }

    @EventHandler fun entityDamageByEntity(e: EntityDamageByEntityEvent) {
        if (e.damager is Player) {
            dispatch(e.damager as Player, (e.damager as Player).inventory.itemInMainHand, e, EventItemType.MAINHAND, e.damager, e.entity)
            // if needed add another dispatch for offhand
        } else if (e.damager is Projectile) {
            // todo: make this not weird as shit
            val customItemId = e.damager.getTag<String>("source") ?: return
            val item = ItemRegistry.get(CustomItem.valueOf(customItemId))
            dispatch(null, item, e)
        }

        if (e.entity is Player) {
            val player = e.entity as Player
            handleArmor(player, e)
        }
        // send to armor stuff? if receiver is player
        // send to weapon stuff if damager is player
        // send to arrow stuff if damager is arrow
    }

    @EventHandler fun playerInteract(e: PlayerInteractEvent) {
        val hand = if (e.hand == EquipmentSlot.OFF_HAND) EventItemType.OFFHAND else EventItemType.MAINHAND
        dispatch(e.player, e.item, e, hand, e.player)
    }

    @EventHandler fun projectileLaunch(e: ProjectileLaunchEvent) {
        val player = e.entity.shooter as? Player ?: return
        val mainhand = player.inventory.itemInMainHand
        val offhand = player.inventory.itemInOffHand
        if (mainhand.type in arrayOf(Material.BOW, Material.CROSSBOW) && mainhand.offCooldown(player))
            dispatch(player, mainhand, e, EventItemType.MAINHAND)
        else
            dispatch(player, offhand, e, EventItemType.OFFHAND)
    }

    @EventHandler fun projectileHit(e: ProjectileHitEvent) {
        val player = e.entity.shooter as? Player ?: return
        val customItemId = e.entity.getTag<String>("source") ?: return
        val item = ItemRegistry.get(CustomItem.valueOf(customItemId))
        dispatch(player, item, e, EventItemType.PROJECTILE)
    }

    @EventHandler fun onSwapHands(e: PlayerSwapHandItemsEvent) {
        val player = e.player
        val mainhand = player.inventory.itemInMainHand
        val offhand = player.inventory.itemInOffHand
        dispatch(player, mainhand, e, EventItemType.MAINHAND)
        dispatch(player, offhand, e, EventItemType.OFFHAND)
    }

    @EventHandler fun onCrossbowLoad(e: EntityLoadCrossbowEvent) {
        val player = e.entity as? Player ?: return
        val mainhand = player.inventory.itemInMainHand
        val offhand = player.inventory.itemInOffHand
        if (mainhand.type in arrayOf(Material.BOW, Material.CROSSBOW) && mainhand.offCooldown(player))
            dispatch(player, mainhand, e, EventItemType.MAINHAND)
        else
            dispatch(player, offhand, e, EventItemType.OFFHAND)
    }

    @EventHandler fun onEntityDespawn(e: EntityRemoveEvent) {
        val entity = e.entity
        val customItem = CustomItem.valueOf(entity.getTag<String>("source") ?: return)
        val item = ItemRegistry.get(customItem)
        dispatch(null, item, e)
    }

    @EventHandler fun onBlockMine(e: BlockBreakEvent) {
        val player = e.player
        val item = player.inventory.itemInMainHand
        dispatch(player, item, e, EventItemType.MAINHAND)
    }

    @EventHandler fun onPlayerInteractEntity(e: PlayerInteractEntityEvent) {
        val hand = if (e.hand == EquipmentSlot.OFF_HAND) EventItemType.OFFHAND else EventItemType.MAINHAND
        if (hand == EventItemType.MAINHAND)
            dispatch(e.player, e.player.inventory.itemInMainHand, e, hand, e.player)
        else
            dispatch(e.player, e.player.inventory.itemInOffHand, e, hand, e.player)

        val interacted = e.rightClicked
        val sourceTag = interacted.getTag<String>("source")
        if (sourceTag != null) {
            val item = ItemRegistry.get(CustomItem.valueOf(sourceTag))
            dispatch(e.player, item, e, EventItemType.SUMMONED_ENTITY)
        }
    }

    @EventHandler fun onBowShoot(e: EntityShootBowEvent) {
        val player = e.entity as? Player ?: return
        val item = e.consumable ?: return
        dispatch(player, item, e, EventItemType.PROJECTILE)
    }

    @EventHandler fun entityMountEvent(e: EntityMountEvent) {
        val player = e.entity as? Player ?: return
        handleArmor(player, e)
    }

    @EventHandler fun entityDismountEvent(e: EntityDismountEvent) {
        val player = e.entity as? Player ?: return
        handleArmor(player, e)
    }

    @EventHandler fun playerMoveEvent(e: PlayerMoveEvent) {
        val boots = e.player.inventory.boots ?: return
        dispatch(e.player, boots, e, EventItemType.BOOTS)
    }

    @EventHandler fun toggleFlightEvent(e: PlayerToggleFlightEvent) {
        if (e.player.gameMode in arrayOf(GameMode.ADVENTURE, GameMode.SURVIVAL)) e.isCancelled = true
        val boots = e.player.inventory.boots ?: return
        dispatch(e.player, boots, e, EventItemType.BOOTS)
    }

    @EventHandler fun onApplyPotionEffect(e: EntityPotionEffectEvent) {
        val player = e.entity as? Player ?: return
        handleArmor(player, e)
    }

    @EventHandler fun onTotemPop(e: EntityResurrectEvent) {
        val player = e.entity as? Player ?: return
        handleArmor(player, e)
    }

    @EventHandler fun onPlayerToggleSneak(e: PlayerToggleSneakEvent) {
        val player = e.player
        handleArmor(player, e)
    }

    @EventHandler fun onBlockPlace(e: BlockPlaceEvent) {
        val player = e.player
        val item = e.itemInHand
        val hand = if (e.hand == EquipmentSlot.OFF_HAND) EventItemType.OFFHAND else EventItemType.MAINHAND
        dispatch(player, item, e, hand)
    }

    @EventHandler fun onEntityPlace(e: EntityPlaceEvent) {
        val player = e.player ?: return
        val item = player.inventory.getItem(e.hand)
        val hand = if (e.hand == EquipmentSlot.OFF_HAND) EventItemType.OFFHAND else EventItemType.MAINHAND
        dispatch(player, item, e, hand)
    }

    @EventHandler fun onPlayerSwapItems(e: PlayerItemHeldEvent) {
        val player = e.player
        val currentItem = player.inventory.getItem(e.previousSlot) ?: return
        dispatch(player, currentItem, e, EventItemType.MAINHAND)
    }

    @EventHandler fun onEnemyAggro(e: EntityTargetEvent) {
        val target = e.target as? Player ?: return
        for (player in target.location.getNearbyPlayers(40.0)) {
            handleArmor(player, e)
        }
    }

}