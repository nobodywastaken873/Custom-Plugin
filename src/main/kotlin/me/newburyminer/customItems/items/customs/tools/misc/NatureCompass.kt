package me.newburyminer.customItems.items.customs.tools.misc

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.hasCustom
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.name
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.gui.misc.NatureCompassGui
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Arrow
import org.bukkit.entity.EnderPearl
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.util.*

class NatureCompass: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.NATURE_COMPASS

    private val material = Material.COMPASS
    private val color = arrayOf(12, 133, 14)
    private val name = text("Nature Compass", color)
    private val lore = Utils.loreBlockToList(
        text("Can be used to find biomes, one use only. Right click to open the selection GUI.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    init {
        register(PlayerInteractEvent::class, { e ->
            e.item.isItem(custom) &&
            e.player.offCooldown(custom)
        },
        {e ->
            val item = e.item ?: return@register

            if (e.action != Action.RIGHT_CLICK_BLOCK && e.action != Action.RIGHT_CLICK_AIR) return@register
            if (e.hand != EquipmentSlot.HAND) return@register

            val inventory = NatureCompassGui(e.player, item)
            inventory.open(e.player)
        })
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