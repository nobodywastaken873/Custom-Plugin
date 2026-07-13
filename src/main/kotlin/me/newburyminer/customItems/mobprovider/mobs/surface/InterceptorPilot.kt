package me.newburyminer.customItems.mobprovider.mobs.surface

import me.newburyminer.customItems.entity.hiteffects.effect.CustomKnockbackApply
import me.newburyminer.customItems.entity.hiteffects.effect.ProjectileKnockbackApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaEffectApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.projectile.ProjectileEffectAbility
import me.newburyminer.customItems.mobprovider.ability.projectile.ProjectileHomingAbility
import me.newburyminer.customItems.mobprovider.ability.spell.EffectMissileAbility
import me.newburyminer.customItems.mobprovider.ability.spell.MultiMissileAbility
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern
import org.bukkit.potion.PotionEffectType

object InterceptorPilot: MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.SURFACE
    override val trim: TrimPattern = TrimPattern.EYE
    override val tier: MobTier = MobTier.ELITE
    override val targetRange: Double = 120.0
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.SKELETON) {

        ability(
            ProjectileEffectAbility(
                damage(linear(25.0 to 50.0, ctx), CustomDamageType.PROJECTILE_NO_CD),
                ProjectileKnockbackApply()
            )
        )

        ability(
            ProjectileHomingAbility()
        )

        ability(
            MultiMissileAbility(
                linear(100.0 to 120.0, ctx),
                linear(2 to 4, ctx),
                linear(25.0 to 50.0, ctx),
                CustomKnockbackApply(-1, 0, -1),
                linear(30.0 to 40.0, ctx),
                linear(40 to 30, ctx),
                linear(200 to 150, ctx),
                ParticleTheme.SURFACE,
                stopTime = 4
            )
        )


        health(
            linear(50.0 to 100.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.5, ctx)
        )

        equipment {
            mainhand(Material.BOW)
            offhand(Material.BLAZE_ROD)
            setArmor(colorTheme.color, trim)
        }

    }

}