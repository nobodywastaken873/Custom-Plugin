package me.newburyminer.customItems.mobprovider.ability.creeper

import me.newburyminer.customItems.entity.components.creepers.ArrowBombCreeper
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.CustomDamageApply
import me.newburyminer.customItems.entity.hiteffects.effect.ProjectileKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.mobprovider.MobAbility
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext

class ArrowBombAbility(
    val count: Int,
    val damage: Double,
): MobAbility {

    override fun MobBuilder.applyAbility(ctx: MobContext) {
        component(
            ArrowBombCreeper(
                count,
                HitEffects(
                    CustomDamageApply(damage, CustomDamageType.PROJECTILE_NO_CD),
                    ProjectileKnockbackApply(0.05)
                )
            )
        )

        apply {
            arrowsInBody = 10
        }
    }

}