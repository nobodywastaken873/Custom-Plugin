package me.newburyminer.customItems.items.customs.weapons.projectile

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.projectiles.CustomDamageProjectile
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.CustomDamageApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.inventory.ItemStack

class SniperRifle: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.SNIPER_RIFLE
    private val material = Material.CROSSBOW
    private val color = arrayOf(52, 69, 54)
    private val name = text("Sniper Rifle", color)
    private val lore = Utils.loreBlockToList(
        text("Shoots an extremely high velocity arrow that goes almost exactly straight, and does 13 true damage on hit, with a 50 second cooldown.", Utils.GRAY),
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
            e.entity.velocity = shooter.location.direction.normalize().multiply(50)
            shooter.setCooldown(CustomItem.SNIPER_RIFLE, 50.0)

            EntityWrapperManager.getWrapperorNew(e.entity).addComponent(CustomDamageProjectile(HitEffects(
                CustomDamageApply(14.0, CustomDamageType.ALL_BYPASS, 0, overrideSource = shooter)
            )))
        })
    }

}