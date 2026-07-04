package me.newburyminer.customItems.mobprovider.ability.spell

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

class BasicSlashAbility(
    val count: Int,
    val damage: Double,
    val cooldown: Int,
    val particleTheme: ParticleTheme,
    vararg val extraEffects: HitEffect
): MobAbility {

    override fun MobBuilder.applyAbility(ctx: MobContext) {

        component(
            SlashSpellComponent(
                3.0,
                Math.PI / 3.5,
                count,
                6,
                0.0,
                HitEffects(CustomDamageApply(damage, CustomDamageType.MELEE_NO_CD), VanillaKnockbackApply(), *extraEffects),
                count * 6 + 20,
                cooldown,
                particleTheme
            )
        )

        component(
            SpellCasterComponent(
                0.0
            )
        )

    }

}