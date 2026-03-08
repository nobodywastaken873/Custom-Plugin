package me.newburyminer.customItems.items.customs.tools.mining

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.smelt
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomEnchantments
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.behaviors.VeinFinder
import org.bukkit.Material
import org.bukkit.block.Container
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

class VeinyPickaxe: CustomItemDefinition, VeinFinder {

    override val custom: CustomItem = CustomItem.VEINY_PICKAXE

    private val material = Material.NETHERITE_PICKAXE
    private val color = arrayOf(150, 125, 0)
    private val name = text("Veiny Pickaxe", color)
    private val lore = Utils.loreBlockToList(
        text("Mines up to 32 blocks of the same type connected to the block you break, with a 3 second cooldown.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    init {
        register(BlockBreakEvent::class, { e ->
            e.player.inventory.itemInMainHand.isItem(custom) &&
            e.block.state !is Container &&
            e.player.offCooldown(custom)
        },
        {e ->
            val pickaxe = e.player.inventory.itemInMainHand
            val vein = getConnected(e.block, 32)

            val drops: MutableList<ItemStack> = mutableListOf()
            var total = 1
            for (loc in vein) {
                for (drop in e.block.world.getBlockAt(loc).getDrops(pickaxe, e.player)) drops.add(drop)
                e.block.world.getBlockAt(loc).type = Material.AIR
                if (total < 5) CustomEffects.playSound(loc, e.block.blockData.soundGroup.breakSound, 1.0F, e.block.blockData.soundGroup.pitch)
                total++
            }

            if (pickaxe.itemMeta.hasEnchant(CustomEnchantments.AUTOSMELT)) {
                for (drop in drops) {
                    drop.smelt()
                }
            }
            for (drop in drops) {
                e.block.world.dropItem(e.block.location.clone().add(Vector(0.5, 0.5, 0.5)), drop)
            }
            pickaxe.setCooldown(e.player, 3.0)
        })
    }

}