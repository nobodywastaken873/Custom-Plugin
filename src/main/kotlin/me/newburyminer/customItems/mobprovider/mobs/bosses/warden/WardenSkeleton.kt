package me.newburyminer.customItems.mobprovider.mobs.bosses.warden

import me.newburyminer.customItems.entity.hiteffects.effect.ProjectileKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
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

object WardenSkeleton: MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.WARDEN
    override val trim: TrimPattern = TrimPattern.RIB
    override val tier: MobTier = MobTier.GRUNT
    override val targetRange: Double = 50.0
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.SKELETON) {

        ability(
            ProjectileEffectAbility(
                damage(linear(17.0 to 34.0, ctx), CustomDamageType.PROJECTILE),
                ProjectileKnockbackApply()
            )
        )

        health(
            linear(20.0 to 36.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.2, ctx)
        )

        equipment {
            mainhand(Material.BOW)
            offhand(Material.AIR)
            setArmor(colorTheme.color, trim)
        }

    }

}