package me.newburyminer.customItems.items.customs.tools.placers

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.addItemorDrop
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.*
import me.newburyminer.customItems.systems.materials.MaterialConverterRegistry
import me.newburyminer.customItems.systems.materials.MaterialSystem
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Container
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.inventory.ItemStack

class Containers: CustomItemDefinition, ItemCycler {

    override val custom: CustomItem = CustomItem.CONTAINERS

    private val material = Material.BARREL
    private val color = arrayOf(163, 5, 5)
    private val name = text("Containers", color)
    private val lore = Utils.loreBlockToList(
        text("Redstone Placer:", arrayOf(199, 19, 6)),
        text("Consumes materials from your redstone box.", Utils.GRAY),
        text(""),
        text("While sneaking, scroll forward or back through your hotbar to cycle through all containers and pistons.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is BlockPlaceEvent -> {
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

            is PlayerItemHeldEvent -> {
                val player = ctx.player ?: return
                val item = ctx.item ?: return
                if (player.isSneaking) {
                    cycleItem(item, e)
                    CustomEffects.playSound(player.location, Sound.UI_BUTTON_CLICK, 1.0F, 1.1F)
                }
            }

        }

    }

    override fun getCycleItems(item: ItemStack): Array<Material> {
        return arrayOf(Material.BARREL, Material.HOPPER, Material.CHEST, Material.CRAFTER,
            Material.DISPENSER, Material.DROPPER, Material.NOTE_BLOCK, Material.PISTON, Material.STICKY_PISTON, Material.SLIME_BLOCK)
    }

}