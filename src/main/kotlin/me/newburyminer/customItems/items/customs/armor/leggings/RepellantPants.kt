package me.newburyminer.customItems.items.customs.armor.leggings

import io.papermc.paper.event.entity.EntityMoveEvent
import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.attr
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.customName
import me.newburyminer.customItems.Utils.Companion.getCustom
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.loreList
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setCustomData
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.*
import net.kyori.adventure.text.Component
import org.bukkit.*
import org.bukkit.entity.*
import org.bukkit.event.entity.EntityDismountEvent
import org.bukkit.event.entity.EntityMountEvent
import org.bukkit.event.entity.EntityPotionEffectEvent
import org.bukkit.event.entity.EntityResurrectEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerToggleFlightEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector

class RepellantPants: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.REPELLANT_PANTS

    private val material = Material.NETHERITE_LEGGINGS
    private val color = arrayOf(27, 2, 64)
    private val name = text("Repellant Pants", color)
    private val lore = Utils.loreBlockToList(
        text("When your totem is popped, launch all nearby enemies away.", Utils.GRAY),
    )

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()
        .attr("ARM+8.0LE","ART+4.0LE","ATD+1.0LE","MAH+2.0LE")

    override fun handle(ctx: EventContext) {
        when (val e = ctx.event) {

            is EntityResurrectEvent -> {
                if (ctx.itemType != EventItemType.LEGGINGS) return
                val player = ctx.player ?: return
                if (e.isCancelled) return
                for (entity in player.location.getNearbyEntities(8.0, 8.0, 8.0)) {
                    if (entity == player) continue
                    entity.velocity = entity.velocity.add(entity.location.subtract(player.location).toVector().normalize().multiply(3).add(
                        Vector(0.0, 1.0, 0.0)
                    ))
                }
            }

        }
    }

}
