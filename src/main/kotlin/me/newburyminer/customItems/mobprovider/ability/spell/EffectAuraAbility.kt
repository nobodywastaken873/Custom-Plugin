package me.newburyminer.customItems.mobprovider.ability.spell

import me.newburyminer.customItems.entity.components.projectiles.SimpleEffectAura
import me.newburyminer.customItems.entity.hiteffects.HitEffect
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobAbility
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext

class EffectAuraAbility(
    val radius: Double,
    val height: Double,
    val particleTheme: ParticleTheme,
    val applyPeriod: Int,
    vararg val effect: HitEffect,
): MobAbility {

    override fun MobBuilder.applyAbility(ctx: MobContext) {

        component(
            SimpleEffectAura(
                radius,
                height,
                -1,
                HitEffects(*effect),
                applyPeriod,
                particleTheme,
                null
            )
        )

    }

}