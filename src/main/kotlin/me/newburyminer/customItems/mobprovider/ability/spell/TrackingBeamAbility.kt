package me.newburyminer.customItems.mobprovider.ability.spell

import me.newburyminer.customItems.entity.components.spells.SpellCasterComponent
import me.newburyminer.customItems.entity.components.spells.TrackingBeamComponent
import me.newburyminer.customItems.entity.hiteffects.HitEffect
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.CustomKnockbackApply
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobAbility
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import org.bukkit.util.Vector

class TrackingBeamAbility(
    val range: Double,
    val hitDelay: Int,
    val duration: Int,
    val cooldown: Int,
    val particleTheme: ParticleTheme,
    vararg val effects: HitEffect,
): MobAbility {

    override fun MobBuilder.applyAbility(ctx: MobContext) {

        component(
            TrackingBeamComponent(
                range,
                0.05,
                0.03,
                hitDelay,
                true,
                HitEffects(*effects),
                duration,
                cooldown,
                particleTheme
            )
        )

        component(
            SpellCasterComponent(0.0)
        )

    }

}