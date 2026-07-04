package me.newburyminer.customItems.mobprovider.ability

import me.newburyminer.customItems.entity.components.defensive.LeapDodgeComponent
import me.newburyminer.customItems.entity.components.melee.MeleeCustomHit
import me.newburyminer.customItems.entity.hiteffects.HitEffect
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.mobprovider.MobAbility
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext

class MeleeEffectAbility(
    vararg val hitEffects: HitEffect
): MobAbility {

    override fun MobBuilder.applyAbility(ctx: MobContext) {
        component(
            MeleeCustomHit(
                HitEffects(
                    *hitEffects
                )
            )
        )
    }

}