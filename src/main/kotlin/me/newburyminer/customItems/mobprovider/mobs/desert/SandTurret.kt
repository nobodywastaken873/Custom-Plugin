package me.newburyminer.customItems.mobprovider.mobs.desert

import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.ability.projectile.ProjectileEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.BeamShooterAbility
import me.newburyminer.customItems.mobprovider.ability.spell.TrackingBeamAbility
import org.bukkit.entity.EntityType
import javax.sound.midi.Track

object SandTurret: MobDefinition {

    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.BLAZE) {

        ability(
            TrackingBeamAbility(
                linear(40.0 to 80.0, ctx),
                10,
                400,
                10,
                ParticleTheme.DESERT,
                damage(linear(5.0 to 10.0, ctx), CustomDamageType.MAGIC_NO_CD)
            )
        )

        ability(
            ProjectileEffectAbility(
                damage(linear(25.0 to 45.0, ctx), CustomDamageType.BURNING_NO_CD),
                VanillaKnockbackApply()
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