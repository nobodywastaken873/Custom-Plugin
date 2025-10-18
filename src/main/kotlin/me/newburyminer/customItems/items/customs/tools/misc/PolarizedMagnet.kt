package me.newburyminer.customItems.items.customs.tools.misc

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.hasCustom
import me.newburyminer.customItems.Utils.Companion.name
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.*
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Arrow
import org.bukkit.entity.EnderPearl
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import java.util.*

class PolarizedMagnet: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.POLARIZED_MAGNET

    private val material = Material.IRON_NUGGET
    private val color = arrayOf(255, 36, 36)
    private val name = text("Polarized Magnet", color)
    private val lore = Utils.loreBlockToList(
        text("Hold right click to pull in all nearby entities. Left click while sneaking to toggle an item pull mode that pulls in all nearby items even when you are not holding it.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is PlayerInteractEvent -> {
                val item = ctx.item ?: return
                if (e.action == Action.LEFT_CLICK_AIR || e.action == Action.LEFT_CLICK_BLOCK) {
                    if (ctx.itemType != EventItemType.MAINHAND) return
                    if (!e.player.isSneaking) return
                    if (!e.player.offCooldown(CustomItem.POLARIZED_MAGNET)) return
                    e.player.setTag("polarizedmagnetitempull", !(e.player.getTag<Boolean>("polarizedmagnetitempull")?:false))
                    if (e.player.getTag<Boolean>("polarizedmagnetitempull")!!) item.name(text("Polarized Magnet", arrayOf(36, 36, 255), bold = true))
                    else item.name(text("Polarized Magnet", arrayOf(255, 36, 36), bold = true))
                    CustomEffects.playSound(e.player.location, Sound.BLOCK_CANDLE_PLACE, 1F, 0.9F)
                    e.player.setCooldown(CustomItem.POLARIZED_MAGNET, 0.5)
                } else {
                    if (!ctx.itemType.isHand()) return
                    pullCount[e.player.uniqueId] = 4
                    CustomEffects.playSound(e.player.location, Sound.BLOCK_BUBBLE_COLUMN_WHIRLPOOL_AMBIENT, 1.0F, 1.1F)
                }
            }

        }

    }

    override val extraTasks: Map<Int, (Player) -> Unit>
        get() = mapOf(1 to {player -> polarizedMagnetPull(player)})

    private val pullCount = mutableMapOf<UUID, Int>()
    private fun polarizedMagnetPull(player: Player) {
        val uuid = player.uniqueId
        if ((pullCount[uuid] ?: 0) > 0) {
            for (entity in player.getNearbyEntities(7.0, 7.0, 7.0)) {
                if (entity is EnderPearl || entity is Arrow) continue
                val dist = player.location.subtract(entity.location).toVector()
                dist.multiply(0.05)
                entity.velocity = entity.velocity.add(dist)
            }
            pullCount[uuid] = (pullCount[uuid] ?: 0) - 1
        }
        if (player.getTag<Boolean>("polarizedmagnetitempull") == true && player.hasCustom(CustomItem.POLARIZED_MAGNET)) {
            for (entity in player.getNearbyEntities(12.0, 12.0, 12.0)) {
                if (entity.type != EntityType.ITEM && entity.type != EntityType.EXPERIENCE_ORB) continue
                val dist = player.location.subtract(entity.location).toVector()
                dist.multiply(0.07)
                entity.velocity = entity.velocity.add(dist)
            }
        }
    }

}