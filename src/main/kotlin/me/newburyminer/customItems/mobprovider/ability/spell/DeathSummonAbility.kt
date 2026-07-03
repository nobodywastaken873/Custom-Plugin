package me.newburyminer.customItems.mobprovider.ability.spell

import me.newburyminer.customItems.entity.components.spells.DeathEffectsComponent
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.SpawnMobApply
import me.newburyminer.customItems.mobprovider.MobAbility
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition

class DeathSummonAbility(
    val mob: MobDefinition,
    val count: Int
): MobAbility {

    override fun MobBuilder.apply(ctx: MobContext) {

         component(
             DeathEffectsComponent(
                 HitEffects(
                     SpawnMobApply(
                         mob,
                         count
                     )
                 )
             )
         )

    }

}