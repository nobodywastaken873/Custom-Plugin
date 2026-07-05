package me.newburyminer.customItems.mobprovider.ability.spell

import me.newburyminer.customItems.entity.components.spells.MultiMissileShooterComponent
import me.newburyminer.customItems.entity.components.spells.SpellCasterComponent
import me.newburyminer.customItems.entity.hiteffects.HitEffect
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.CustomDamageApply
import me.newburyminer.customItems.entity.hiteffects.effect.CustomKnockbackApply
import me.newburyminer.customItems.entity.velocity.StoppedStartVelocity
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.HomingSystem
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobAbility
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext

class MultiMissileAbility(
    val range: Double,
    val count: Int,
    val damage: Double,
    val knockback: HitEffect,
    val aggression: Double,
    val castTime: Int,
    val cooldown: Int,
    val particleTheme: ParticleTheme,
    val stopTime: Int = 20
): MobAbility {

    override fun MobBuilder.applyAbility(ctx: MobContext) {

        component(
            MultiMissileShooterComponent(
                range,
                0.05,
                count,
                5,
                StoppedStartVelocity(0.2 * aggression, 0.05 * aggression, HomingSystem.Type.BOTH_SCALED, stopTime, 1.5),
                HitEffects(
                    CustomDamageApply(damage, CustomDamageType.PROJECTILE_NO_CD),
                    knockback
                ),
                castTime + 5 * count,
                cooldown,
                particleTheme
            )
        )

        component(
            SpellCasterComponent()
        )

    }
    
}