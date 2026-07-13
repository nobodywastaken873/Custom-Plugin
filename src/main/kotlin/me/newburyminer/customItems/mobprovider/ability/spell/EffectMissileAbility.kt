package me.newburyminer.customItems.mobprovider.ability.spell

import me.newburyminer.customItems.entity.components.spells.MagicMissileShooterComponent
import me.newburyminer.customItems.entity.components.spells.SpellCasterComponent
import me.newburyminer.customItems.entity.hiteffects.HitEffect
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.velocity.DelaylessConstantVelocity
import me.newburyminer.customItems.helpers.HomingSystem
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobAbility
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext

class EffectMissileAbility(
    val range: Double,
    val size: Double,
    val aggression: Double,
    val castTime: Int,
    val cooldown: Int,
    val particleTheme: ParticleTheme,
    vararg val hitEffects: HitEffect
): MobAbility {

    override fun MobBuilder.applyAbility(ctx: MobContext) {

        component(
            MagicMissileShooterComponent(
                range,
                size,
                DelaylessConstantVelocity(
                    aggression * 0.25,
                    aggression * 0.05,
                    HomingSystem.Type.BOTH_SCALED
                ),
                HitEffects(*hitEffects),
                castTime,
                cooldown,
                particleTheme
            )
        )

        component(
            SpellCasterComponent(
                -0.2
            )
        )

    }

}