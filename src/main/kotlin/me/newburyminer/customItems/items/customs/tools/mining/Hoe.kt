package me.newburyminer.customItems.items.customs.tools.mining

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.addItemorDrop
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.*
import me.newburyminer.customItems.items.behaviors.CubeHarvester
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Tag
import org.bukkit.block.data.Ageable
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

class Hoe: CustomItemDefinition, CubeHarvester {

    override val custom: CustomItem = CustomItem.HOE

    private val material = Material.NETHERITE_HOE
    private val color = arrayOf(37, 110, 41)
    private val name = text("H.O.E.", color)
    private val lore = Utils.loreBlockToList(
        text("Turns all dirt-like tiles in a 3x3x3 area around the block you till into farmland, and harvests all crops in a 3x3x3 area when you break a crop, leaving non-fully grown crops untouched.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    init {
        register(PlayerInteractEvent::class, { e ->
            e.item.isItem(custom) &&
            e.action == Action.RIGHT_CLICK_BLOCK &&
            Tag.DIRT.isTagged(e.clickedBlock!!.type)
        }, 
        {e ->
            for (loc in getAround(e.clickedBlock?.location ?: return@register)) {
                if (e.player.world.getBlockAt(loc.clone().add(Vector(0, 1, 0))).type != Material.AIR) continue
                if (!Tag.DIRT.isTagged(e.player.world.getBlockAt(loc).type)) continue
                e.player.world.getBlockAt(loc).type = Material.FARMLAND
            }
        })

        register(BlockBreakEvent::class, { e ->
            e.player.inventory.itemInMainHand.isItem(custom) &&
            (
                Tag.CROPS.isTagged(e.block.type) ||
                e.block.type in arrayOf(Material.BAMBOO, Material.COCOA, Material.PITCHER_CROP)
            )
        },
        {e ->
            e.isCancelled = true
            val blocks = getAround(e.block.location, true)
            val drops: MutableList<ItemStack> = mutableListOf()
            for (loc in blocks) {
                val block = e.block.world.getBlockAt(loc)
                if (!Tag.CROPS.isTagged(block.type) && block.type !in arrayOf(Material.BAMBOO, Material.COCOA, Material.PITCHER_CROP)) continue
                val newMeta = block.blockData as Ageable
                if (newMeta.age != newMeta.maximumAge) continue
                for (drop in block.getDrops(e.player.inventory.itemInMainHand, e.player)) drops.add(drop)
                newMeta.age = 0
                block.blockData = newMeta
            }
            for (drop in drops) e.player.addItemorDrop(drop)
        })
    }
    
    /*override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is PlayerInteractEvent -> {
                if (!ctx.itemType.isHand()) return
                val player = ctx.player ?: return
                if (e.action != Action.RIGHT_CLICK_BLOCK) return
                if (!Tag.DIRT.isTagged(e.clickedBlock?.type ?: Material.AIR)) return
                /*if (e.player.world.getBlockAt(e.clickedBlock!!.location.add(0.0, 1.0, 0.0)).type == Material.AIR) {
                    clickedBlock.type = Material.FARMLAND
                }*/
                e.isCancelled = false
                for (loc in getAround(e.clickedBlock?.location ?: return)) {
                    if (player.world.getBlockAt(loc.clone().add(Vector(0, 1, 0))).type != Material.AIR) continue
                    if (!Tag.DIRT.isTagged(e.player.world.getBlockAt(loc).type)) continue
                    player.world.getBlockAt(loc).type = Material.FARMLAND
                }
            }

            is BlockBreakEvent -> {
                if (ctx.itemType != EventItemType.MAINHAND) return
                val hoe = ctx.item ?: return
                if (!Tag.CROPS.isTagged(e.block.type) && e.block.type !in arrayOf(Material.BAMBOO, Material.COCOA, Material.PITCHER_CROP)) return
                e.isCancelled = true
                val blocks = getAround(e.block.location, true)
                val drops: MutableList<ItemStack> = mutableListOf()
                for (loc in blocks) {
                    val block = e.block.world.getBlockAt(loc)
                    if (!Tag.CROPS.isTagged(block.type) && block.type !in arrayOf(Material.BAMBOO, Material.COCOA, Material.PITCHER_CROP)) continue
                    val newMeta = block.blockData as Ageable
                    if (newMeta.age != newMeta.maximumAge) continue
                    for (drop in block.getDrops(hoe, e.player)) drops.add(drop)
                    newMeta.age = 0
                    block.blockData = newMeta
                }
                for (drop in drops) e.player.addItemorDrop(drop)
            }

        }

    }*/

}