package me.newburyminer.customItems.mobprovider.mobs.desert

import me.newburyminer.customItems.entity.components.defensive.DamageBlockerComponent
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaEffectApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.defensive.DamageShieldAbility
import me.newburyminer.customItems.mobprovider.ability.projectile.ProjectileEffectAbility
import me.newburyminer.customItems.mobprovider.ability.projectile.ProjectileHomingAbility
import me.newburyminer.customItems.mobprovider.ability.spell.BeamShooterAbility
import org.bukkit.entity.EntityType

object AntlionSpirit : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.DESERT
	override val tier: MobTier = MobTier.ELITE
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.BLAZE) {

        ability(
            BeamShooterAbility(
                linear(20.0 to 30.0, ctx),
                false,
                linear(40 to 20, ctx),
                linear(100 to 80, ctx),
                ParticleTheme.DESERT,
                damage(linear(25.0 to 45.0, ctx), CustomDamageType.MAGIC_NO_CD),
                VanillaKnockbackApply(0.8)
            )
        )

        ability(
            BeamShooterAbility(
                linear(30.0 to 40.0, ctx),
                true,
                linear(60 to 40, ctx),
                linear(200 to 160, ctx),
                ParticleTheme.DESERT,
                damage(linear(20.0 to 40.0, ctx), CustomDamageType.MAGIC_NO_CD),
                VanillaKnockbackApply(0.8)
            )
        )

        ability(
            ProjectileHomingAbility()
        )

        ability(
            ProjectileEffectAbility(
                damage(linear(25.0 to 45.0, ctx), CustomDamageType.BURNING_NO_CD),
                VanillaKnockbackApply()
            )
        )

        ability(
            DamageShieldAbility(
                linear(2 to 4, ctx),
                linear(80 to 60, ctx),
                ParticleTheme.DESERT,
            )
        )

        health(
            linear(180.0 to 360.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.5, ctx)
        )

    }

}