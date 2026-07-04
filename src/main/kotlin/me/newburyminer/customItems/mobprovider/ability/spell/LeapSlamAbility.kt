package me.newburyminer.customItems.mobprovider.ability.spell

import me.newburyminer.customItems.entity.components.spells.LeapSlamComponent
import me.newburyminer.customItems.entity.components.spells.SpellCasterComponent
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.CustomDamageApply
import me.newburyminer.customItems.entity.hiteffects.effect.CustomKnockbackApply
import me.newburyminer.customItems.entity.hiteffects.effect.DamageRadiusApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobAbility
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import org.bukkit.util.Vector

class LeapSlamAbility(
    val minDistance: Double,
    val extraHeight: Double,
    val radius: Double,
    val damage: Double,
    val knockback: Double,
    val cooldown: Int,
    val castTime: Int,
    val particleTheme: ParticleTheme
): MobAbility {

    override fun MobBuilder.applyAbility(ctx: MobContext) {

        component(

            LeapSlamComponent(
                minDistance,
                extraHeight,
                HitEffects(
                    DamageRadiusApply(
                        radius,
                        1.0,
                        HitEffects(
                            CustomDamageApply(damage, CustomDamageType.MELEE_NO_CD),
                            CustomKnockbackApply(Vector(knockback, knockback * 0.5, knockback))
                        ),
                        particleTheme
                    )
                ),
                cooldown,
                castTime
            )

        )

        component(
            SpellCasterComponent()
        )

    }

}