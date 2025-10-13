package me.newburyminer.customItems.items.customs.tools.placers

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.registry.keys.tags.DamageTypeTagKeys
import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.addItemorDrop
import me.newburyminer.customItems.Utils.Companion.attr
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.customName
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.isBeingTracked
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.loreBlock
import me.newburyminer.customItems.Utils.Companion.loreList
import me.newburyminer.customItems.Utils.Companion.name
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.pushOut
import me.newburyminer.customItems.Utils.Companion.reduceDura
import me.newburyminer.customItems.Utils.Companion.resist
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setCustomData
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.smelt
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.Utils.Companion.unb
import me.newburyminer.customItems.entities.CustomEntity
import me.newburyminer.customItems.entities.bosses.BossListeners
import me.newburyminer.customItems.entities.bosses.CustomBoss
import me.newburyminer.customItems.gui.GuiInventory
import me.newburyminer.customItems.gui.MaterialsHolder
import me.newburyminer.customItems.helpers.AttributeManager.Companion.tempAttribute
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.*
import me.newburyminer.customItems.systems.materials.MaterialCategory
import me.newburyminer.customItems.systems.materials.MaterialConverterRegistry
import me.newburyminer.customItems.systems.materials.MaterialSystem
import net.kyori.adventure.text.Component
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.block.Container
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.*
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityPlaceEvent
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector

class RedstoneAmalgamation: CustomItemDefinition, ItemCycler {

    override val custom: CustomItem = CustomItem.REDSTONE_AMALGAMATION

    private val material = Material.REDSTONE_BLOCK
    private val color = arrayOf(92, 85, 81)
    private val name = text("Redstone Amalgamation", color)
    private val lore = Utils.loreBlockToList(
        text("Redstone Placer:", arrayOf(199, 19, 6)),
        text("Consumes materials from your redstone box.", Utils.GRAY),
        text(""),
        text("While sneaking, scroll forward or back through your hotbar to cycle through the items within a group.", Utils.GRAY),
        text("This item contains all of the other redstone placers, to swap groups, swap hands while sneaking.", Utils.GRAY),
    )

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()
        .setTag("storedinner", arrayOf(0, 0, 0, 0).toIntArray())
        .setTag("redstonegroup", 0)
        .setTag("toolindex", 0)

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is BlockPlaceEvent -> {
                if (!ctx.itemType.isHand()) return
                val item = ctx.item ?: return
                val player = ctx.player ?: return
                val collection = MaterialConverterRegistry.convert(item)
                if (collection == null) {e.isCancelled = true; return}

                if (!MaterialSystem.hasMaterials(player, collection)) {
                    e.isCancelled = true
                    player.sendActionBar(text("Not enough materials in your Redstone Box.", Utils.FAILED_COLOR))
                    CustomEffects.playSound(player.location, Sound.ENTITY_VILLAGER_NO, 1F, 0.8F)
                    return
                }

                MaterialSystem.removeMaterials(player, collection)

                val slot = if (e.itemInHand == player.inventory.itemInMainHand) player.inventory.heldItemSlot else 40
                val savedItem = ItemStack(e.itemInHand).clone()
                Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
                    if (player.inventory.getItem(slot)?.type == Material.AIR || player.inventory.getItem(slot) == null)
                        player.inventory.setItem(slot, savedItem)
                    else
                        player.addItemorDrop(savedItem)

                    if (e.blockPlaced.state is Container) {
                        val newState = (e.block.state as Container)
                        newState.customName(null)
                        newState.update()
                    }
                })
            }

            is EntityPlaceEvent -> {
                if (!ctx.itemType.isHand()) return
                val item = ctx.item ?: return
                val player = ctx.player ?: return
                val collection = MaterialConverterRegistry.convert(item)
                if (collection == null) {e.isCancelled = true; return}

                if (!MaterialSystem.hasMaterials(player, collection)) {
                    e.isCancelled = true
                    player.sendActionBar(text("Not enough materials in your Redstone Box.", Utils.FAILED_COLOR))
                    CustomEffects.playSound(player.location, Sound.ENTITY_VILLAGER_NO, 1F, 0.8F)
                    return
                }

                MaterialSystem.removeMaterials(player, collection)

                val slot = if (item == player.inventory.itemInMainHand) player.inventory.heldItemSlot else 40
                val savedItem = ItemStack(item).clone()
                Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
                    if (player.inventory.getItem(slot)?.type == Material.AIR || player.inventory.getItem(slot) == null)
                        player.inventory.setItem(slot, savedItem)
                    else
                        player.addItemorDrop(savedItem)
                })
            }

            is PlayerItemHeldEvent -> {
                if (ctx.itemType != EventItemType.MAINHAND) return
                val player = ctx.player ?: return
                val item = ctx.item ?: return
                if (player.isSneaking) {
                    cycleItem(item, e)
                    CustomEffects.playSound(player.location, Sound.UI_BUTTON_CLICK, 1.0F, 1.1F)
                }
            }

            is PlayerSwapHandItemsEvent -> {
                if (ctx.itemType != EventItemType.MAINHAND) return
                if (!e.player.isSneaking) return
                val item = ctx.item ?: return
                val currentGroup = item.getTag<Int>("redstonegroup") ?: 0
                val newGroup = if (currentGroup == 3) 0 else currentGroup + 1
                val storedIndexes = item.getTag<IntArray>("storedinner") ?: return
                val currentIndex = item.getTag<Int>("toolindex") ?: 0
                val newIndex = storedIndexes[newGroup]
                storedIndexes[currentGroup] = currentIndex

                item.setTag("redstonegroup", newGroup)
                item.setTag("toolindex", newIndex)
                item.setTag("storedinner", storedIndexes)
                item.type = getCycleItems(item)[newIndex]
                e.isCancelled = true
            }

        }

    }

    override fun getCycleItems(item: ItemStack): Array<Material> {
        return when (item.getTag<Int>("redstonegroup") ?: 0) {
            0 -> arrayOf(Material.STONE_BUTTON, Material.OAK_BUTTON, Material.LEVER, Material.OAK_PRESSURE_PLATE,
                Material.STONE_PRESSURE_PLATE, Material.LIGHT_WEIGHTED_PRESSURE_PLATE, Material.HEAVY_WEIGHTED_PRESSURE_PLATE)
            1 -> arrayOf(Material.MINECART, Material.DETECTOR_RAIL, Material.RAIL, Material.POWERED_RAIL,
                Material.ACTIVATOR_RAIL, Material.HOPPER_MINECART, Material.CHEST_MINECART, Material.FURNACE_MINECART, Material.TNT_MINECART)
            2 -> arrayOf(Material.REDSTONE, Material.REDSTONE_BLOCK, Material.REPEATER, Material.COMPARATOR,
                Material.REDSTONE_TORCH, Material.OBSERVER)
            else -> arrayOf(Material.BARREL, Material.HOPPER, Material.CHEST, Material.CRAFTER,
                Material.DISPENSER, Material.DROPPER, Material.NOTE_BLOCK, Material.PISTON, Material.STICKY_PISTON, Material.SLIME_BLOCK)
        }
    }

}
