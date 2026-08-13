package me.newburyminer.customItems.items.customs.weapons.projectile

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.inventory.ItemStack

class RidableCrossbow: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.RIDABLE_CROSSBOW

    private val material = Material.CROSSBOW
    private val color = arrayOf(173, 94, 49)
    private val name = text("Ridable Crossbow", color)
    private val lore = Utils.loreBlockToList(
        text("Shoot to launch an arrow that you will ride on. 60s cooldown.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    init {
        register(ProjectileLaunchEvent::class, { e ->
            activeRangedMatches(e, custom)
        },
        {e ->
            val shooter = e.entity.shooter as? Player ?: return@register
            e.entity.velocity = e.entity.velocity.multiply(1.2)
            shooter.setCooldown(CustomItem.RIDABLE_CROSSBOW, 60.0)
            e.entity.addPassenger(shooter)
        })
    }

}