package me.newburyminer.customItems.items.customs.weapons.projectile

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.customName
import me.newburyminer.customItems.Utils.Companion.loreList
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setCustomData
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import org.bukkit.Material
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.inventory.ItemStack

class RidableCrossbow: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.RIDABLE_CROSSBOW

    private val material = Material.CROSSBOW
    private val color = arrayOf(173, 94, 49)
    private val name = text("Ridable Crossbow", color)
    private val lore = Utils.loreBlockToList(
        text("Shoot to launch an arrow that you will ride on.", Utils.GRAY),
    )

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is ProjectileLaunchEvent -> {
                val shooter = ctx.player ?: return
                var crossbow = ctx.item ?: return
                if (!shooter.offCooldown(CustomItem.RIDABLE_CROSSBOW)) {e.isCancelled = true; return}

                e.entity.velocity = e.entity.velocity.multiply(1.2)
                shooter.setCooldown(CustomItem.RIDABLE_CROSSBOW, 60.0)
                e.entity.addPassenger(shooter)
            }

        }

    }

}