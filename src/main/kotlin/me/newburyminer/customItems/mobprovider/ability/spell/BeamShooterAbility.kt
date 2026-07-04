package me.newburyminer.customItems.mobprovider.ability.spell

import me.newburyminer.customItems.entity.components.spells.LaserBeamComponent
import me.newburyminer.customItems.entity.hiteffects.HitEffect
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobAbility
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext

class BeamShooterAbility(
    val range: Double,
    val followPlayer: Boolean,
    val castTime: Int,
    val cooldown: Int,
    val particleTheme: ParticleTheme,
    vararg val hitEffects: HitEffect,
): MobAbility {

    override fun MobBuilder.applyAbility(ctx: MobContext) {

        component(
            LaserBeamComponent(
                range,
                0.0,
                followPlayer,
                true,
                HitEffects(*hitEffects),
                castTime,
                cooldown,
                particleTheme
            )
        )

    }

}