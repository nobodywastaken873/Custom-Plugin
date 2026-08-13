package me.newburyminer.customItems.items.customs.tools.misc

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.beautify
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.isInCombat
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.removeTag
import me.newburyminer.customItems.Utils.Companion.setCount
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.gui.CustomGui
import me.newburyminer.customItems.gui.crafting.CraftingGui
import me.newburyminer.customItems.gui.inventory.ShulkerGui
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.helpers.getUpperCenter
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.EntityEffect
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class ItemNode: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.ITEM_NODE

    private val material = Material.IRON_NAUTILUS_ARMOR
    private val nameColor = arrayOf(166, 139, 173)
    private val name = text("Item Node", nameColor)
    private val lore = Utils.loreBlockToList(
        text("Stored Item: NONE, Count: 0", Utils.GRAY),
        text("Auto-pickup: DISABLED", Utils.GRAY),
        text(""),
        text("Can store an infinite amount of any one item. Right click in your inventory to deposit a stack of items. " +
            "Right click with an empty cursor to retrieve a stack. Can also automatically deposit picked-up items, sneak-left click in your mainhand to toggle. " +
            "Sneak right click to dump up to 150 stacks on the ground. Does not work in combat.", Utils.GRAY)
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .hideAttributes()
        .build()

    init {
        register(InventoryClickEvent::class, { e ->
            e.whoClicked is Player &&
            !(e.whoClicked as Player).isInCombat() &&
            (e.action == InventoryAction.PICKUP_HALF || e.action == InventoryAction.SWAP_WITH_CURSOR) &&
            (e.inventory.holder !is CustomGui || e.inventory.holder is ShulkerGui || e.inventory.holder is CraftingGui) &&
            e.clickedInventory?.getItem(e.slot).isItem(custom)
        }, 
        {e ->
            val player = e.whoClicked as Player
            e.isCancelled = true

            if (e.cursor.type != Material.AIR && e.action == InventoryAction.SWAP_WITH_CURSOR) {

                val node = e.clickedInventory?.getItem(e.slot) ?: return@register
                val storedItem = node.getTag<ItemStack>("storeditem")
                val count = node.getTag<Int>("count") ?: 0

                if (count == 0) {
                    node.setTag("storeditem", e.cursor)
                }
                if (e.cursor.isSimilar(storedItem) || count == 0) {
                    node.setTag("count", count + e.cursor.amount)
                    e.cursor.amount = 0
                    CustomEffects.playSoundToPlayer(player, Sound.ENTITY_ITEM_PICKUP, 0.5F, 0.75F)
                    updateLore(node)
                }

            }
            else {

                val node = e.clickedInventory?.getItem(e.slot) ?: return@register
                val storedItem = node.getTag<ItemStack>("storeditem")
                val count = node.getTag<Int>("count") ?: 0

                if (count > 0 && storedItem != null) {
                    val maxCount = storedItem.maxStackSize
                    val newCount = count.coerceAtMost(maxCount)
                    e.view.setCursor(storedItem.clone().setCount(newCount))

                    node.setTag("count", count - newCount)
                    if (count - newCount == 0) {
                        storedItem.removeTag("storeditem")
                    }

                    updateLore(node)
                    CustomEffects.playSoundToPlayer(player, Sound.ENTITY_ITEM_PICKUP, 0.5F, 1.25F)
                }

            }

        })

        register(PlayerInteractEvent::class, { e ->
            e.item.isItem(custom) &&
            e.player.isSneaking
        },
        {e ->

            val node = e.item ?: return@register
            val storedItem = node.getTag<ItemStack>("storeditem")
            val count = node.getTag<Int>("count") ?: 0

            if (e.action == Action.RIGHT_CLICK_AIR || e.action == Action.RIGHT_CLICK_BLOCK) {

                if (count == 0 || storedItem == null) return@register
                if (e.player.location.getNearbyEntitiesByType(Item::class.java, 20.0).size > 150) return@register
                val maxStack = storedItem.maxStackSize

                val stackCount = (count / maxStack).coerceAtMost(150)
                val extra = count % maxStack

                repeat(stackCount) {
                    e.player.world.spawn(e.player.getUpperCenter(), Item::class.java) {
                        it.velocity = e.player.location.direction.normalize().multiply(0.5)
                        it.itemStack = storedItem.clone().setCount(maxStack)
                    }
                }

                e.player.world.spawn(e.player.getUpperCenter(), Item::class.java) {
                    it.velocity = e.player.location.direction.normalize().multiply(0.5)
                    it.itemStack = storedItem.clone().setCount(extra)
                }

                val finalCount = count - (stackCount * maxStack + extra)
                node.setTag("count", finalCount)
                if (finalCount == 0) {
                    node.removeTag("storeditem")
                }
                updateLore(node)

                e.player.playSound(e.player, Sound.ITEM_BUNDLE_DROP_CONTENTS, 0.5F, 1.0F)

            }
            else if (e.action == Action.LEFT_CLICK_AIR || e.action == Action.LEFT_CLICK_BLOCK) {

                val pickupActive = node.getTag<Boolean>("pickupactive") ?: false
                node.setTag("pickupactive", !pickupActive)
                e.player.playSound(e.player, Sound.BLOCK_POINTED_DRIPSTONE_FALL, 0.5F, 1.4F)
                updateLore(node)

            }

        })

        register(EntityPickupItemEvent::class, { e ->
            e.entity is Player
        },
        {e ->

            val player = e.entity as Player
            val inventory = player.inventory.contents
            if (inventory.none { it?.isItem(custom) == true }) return@register

            val nodes = inventory.filterNotNull().filter { it.isItem(custom) }
            nodes.forEach { node ->
                if (node.getTag<Boolean>("pickupactive") != true) return@forEach

                val storedItem = node.getTag<ItemStack>("storeditem")
                val count = node.getTag<Int>("count") ?: 0

                if (e.item.itemStack.isSimilar(storedItem) && count > 0) {
                    node.setTag("count", count + e.item.itemStack.amount)

                    e.isCancelled = true
                    e.item.remove()
                    e.item.location.world.playSound(e.item.location, Sound.ENTITY_ITEM_PICKUP, 0.5F, 1.25F)
                    e.item.itemStack.amount = 0

                    updateLore(node)
                }
            }

        })

    }

    private fun updateLore(node: ItemStack) {
        val storedItem = node.getTag<ItemStack>("storeditem")
        val count = node.getTag<Int>("count") ?: 0
        val pickupActive = node.getTag<Boolean>("pickupactive") ?: false

        if (count == 0) {
            node.removeTag("storeditem")
        }

        val itemName =
            if (count == 0) "NONE"
            else storedItem?.type?.name?.beautify() ?: "NONE"
        val pickup = if (pickupActive) "ENABLED" else "DISABLED"

        node.lore(
            Utils.loreBlockToList(
                text("Stored Item: $itemName, Count: $count", Utils.GRAY),
                text("Auto-pickup: $pickup", Utils.GRAY),
                text(""),
                text("Can store an infinite amount of any one item. Right click in your inventory to deposit a stack of items. " +
                        "Right click with an empty cursor to retrieve a stack. Can also automatically deposit picked-up items, sneak-left click in your mainhand to toggle. " +
                        "Sneak right click to dump up to 150 stacks on the ground. Does not work in combat.", Utils.GRAY)
            )
        )
    }

}