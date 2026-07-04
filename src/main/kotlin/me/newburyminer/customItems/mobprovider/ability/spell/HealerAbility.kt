package me.newburyminer.customItems.mobprovider.ability.spell

import me.newburyminer.customItems.entity.components.spells.MobHealerComponent
import me.newburyminer.customItems.entity.components.spells.SlashSpellComponent
import me.newburyminer.customItems.entity.components.spells.SpellCasterComponent
import me.newburyminer.customItems.entity.hiteffects.HitEffect
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.CustomDamageApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobAbility
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext

class HealerAbility(
    val range: Double,
    val healAmount: Double,
    val absorptionAmount: Double,
    val castTime: Int,
    val cooldown: Int,
    vararg val extraEffects: HitEffect
): MobAbility {

    override fun MobBuilder.applyAbility(ctx: MobContext) {

        component(
            MobHealerComponent(
                range,
                healAmount,
                absorptionAmount,
                HitEffects(*extraEffects),
                castTime,
                cooldown,
            )
        )

        component(
            SpellCasterComponent(
                -0.4
            )
        )

    }

}