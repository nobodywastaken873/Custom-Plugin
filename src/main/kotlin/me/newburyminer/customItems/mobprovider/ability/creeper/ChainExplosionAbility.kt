package me.newburyminer.customItems.mobprovider.ability.creeper

import me.newburyminer.customItems.entity.components.creepers.ChainExplosionCreeper
import me.newburyminer.customItems.mobprovider.MobAbility
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext

class ChainExplosionAbility(): MobAbility {

    override fun MobBuilder.apply(ctx: MobContext) {
        component(
            ChainExplosionCreeper()
        )
    }

}