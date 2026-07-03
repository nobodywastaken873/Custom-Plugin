package me.newburyminer.customItems.mobprovider.ability.spell

import me.newburyminer.customItems.entity.components.spells.EffectAuraCaster
import me.newburyminer.customItems.entity.components.spells.SpellCasterComponent
import me.newburyminer.customItems.entity.hiteffects.HitEffect
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.CustomDamageApply
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobAbility
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition

class DamageAuraCastAbility(
    val radius: Double,
    val height: Double,
    val anchoredTime: Int,
    val startupTime: Int,
    val durationAfter: Int,
    val applyPeriod: Int,
    val particleTheme: ParticleTheme,
    val castTime: Int,
    val cooldown: Int,
    val range: Double,
    vararg val damage: HitEffect,
): MobAbility {

    override fun MobBuilder.apply(ctx: MobContext) {

        component(
            EffectAuraCaster(
                radius,
                height,
                anchoredTime + startupTime + durationAfter,
                anchoredTime + startupTime,
                anchoredTime,
                HitEffects(*damage),
                applyPeriod,
                particleTheme,
                castTime,
                cooldown,
                range
            )
        )

        component(
            SpellCasterComponent(-0.2)
        )

    }

}