package me.newburyminer.customItems.mobprovider.mobs.caves

import me.newburyminer.customItems.entity.hiteffects.effect.ExplosionApply
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.projectile.ProjectileEffectAbility
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern

object RPGSkeleton: MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.CAVES
    override val trim: TrimPattern = TrimPattern.BOLT
    override val tier: MobTier = MobTier.GRUNT
    override val targetRange: Double = 55.0
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.SKELETON) {

        ability(
            ProjectileEffectAbility(
                ExplosionApply(
                    linear(3.0 to 6.0, ctx).toFloat(),
                    true
                )
            )
        )

        health(
            linear(35.0 to 70.0, ctx)
        )

        movementSpeed(
            linear(1.4 to 1.8, ctx)
        )
        
        equipment {
            mainhand(Material.BOW)
            offhand(Material.GUNPOWDER)
            setArmor(colorTheme.color, trim)
        }

    }

}