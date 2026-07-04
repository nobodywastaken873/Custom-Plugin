package me.newburyminer.customItems.mobprovider.ability.projectile

import me.newburyminer.customItems.entity.components.melee.MeleeCustomHit
import me.newburyminer.customItems.entity.components.projectileshooters.ProjectileDamageShooter
import me.newburyminer.customItems.entity.hiteffects.HitEffect
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.mobprovider.MobAbility
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext

class ProjectileEffectAbility(
    vararg hitEffect: HitEffect
): MobAbility {

    val hitEffects = hitEffect

    override fun MobBuilder.applyAbility(ctx: MobContext) {
        component(
            ProjectileDamageShooter(
                HitEffects(
                    *hitEffects
                )
            )
        )
    }

}