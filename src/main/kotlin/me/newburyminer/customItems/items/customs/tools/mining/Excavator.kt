package me.newburyminer.customItems.items.customs.tools.mining

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.smelt
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomEnchantments
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.behaviors.CubeHarvester
import org.bukkit.Material
import org.bukkit.block.Container
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

class Excavator: CustomItemDefinition, CubeHarvester {

    override val custom: CustomItem = CustomItem.EXCAVATOR

    private val material = Material.NETHERITE_PICKAXE
    private val color = arrayOf(79, 66, 67)
    private val name = text("Excavator", color)
    private val lore = Utils.loreBlockToList(
        text("Mines a 3x3x3 area around the block that you break.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    init {
        register(BlockBreakEvent::class, { e ->
            e.player.inventory.itemInMainHand.isItem(custom) &&
            e.player.getTag<Boolean>("excavatoractive") != true
        },
        {e ->
            val pickaxe = e.player.inventory.itemInMainHand

            if (e.player.world == CustomItems.aridWorld) {
                e.player.setTag("excavatoractive", true)
                val toBreak = getAround(e.block.location)
                toBreak.forEach {
                    e.player.breakBlock(it.block)
                }
                e.player.setTag("excavatoractive", false)
                return@register
            }

            val drops = mutableListOf<ItemStack>()
            for (block in getAround(e.block.location)) {
                if (e.block.world.getBlockAt(block).type.hardness.toInt() == -1) continue
                if (e.block.world.getBlockAt(block).state is Container) continue

                for (drop in block.block.getDrops(pickaxe, e.player)) drops.add(drop)
                e.block.world.getBlockAt(block).type = Material.AIR
            }
            if (CustomEnchantments.AUTOSMELT in e.player.inventory.itemInMainHand.enchantments) {
                for (drop in drops) {
                    drop.smelt()
                }
            }
            for (drop in drops) {
                e.block.world.dropItem(e.block.location.clone().add(Vector(0.5, 0.5, 0.5)), drop)
            }
        })
    }

}