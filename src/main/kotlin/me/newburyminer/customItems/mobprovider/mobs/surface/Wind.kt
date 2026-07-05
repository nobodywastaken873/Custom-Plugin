package me.newburyminer.customItems.mobprovider.mobs.surface

import me.newburyminer.customItems.entity.hiteffects.effect.CustomKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.defensive.LeapDodgeAbility
import me.newburyminer.customItems.mobprovider.ability.projectile.ProjectileHomingAbility
import me.newburyminer.customItems.mobprovider.ability.spell.BeamShooterAbility
import org.bukkit.entity.EntityType

object Wind: MobDefinition() {

    override val tier: MobTier = MobTier.STANDARD
    override val targetRange: Double = 50.0
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.BREEZE) {

        ability(
            ProjectileHomingAbility()
        )

        ability(
            BeamShooterAbility(
                linear(30.0 to 40.0, ctx),
                true,
                linear(50 to 40, ctx),
                linear(200 to 180, ctx),
                ParticleTheme.SURFACE,
                damage(linear(22.0 to 44.0, ctx), CustomDamageType.PROJECTILE_NO_CD),
                CustomKnockbackApply(0.0, 1.2, 0.0)
            )
        )

        ability(
            LeapDodgeAbility(
                linear(0.2 to 0.4, ctx),
                linear(200 to 100, ctx),
                0.7
            )
        )

        health(
            linear(60.0 to 120.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.5, ctx)
        )

    }

}