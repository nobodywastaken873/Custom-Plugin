package me.newburyminer.customItems.mobprovider.ability.creeper

import me.newburyminer.customItems.entity.components.creepers.HoppingCreeper
import me.newburyminer.customItems.mobprovider.MobAbility
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext

class CreeperHopAbility(): MobAbility {

    override fun MobBuilder.applyAbility(ctx: MobContext) {
        component(
            HoppingCreeper()
        )
    }

}