package me.newburyminer.customItems.mobprovider.ability.creeper

import me.newburyminer.customItems.entity.components.creepers.FireworkCreeper
import me.newburyminer.customItems.mobprovider.MobAbility
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext

class FireworkBombAbility(
    val count: Int,
    val damage: Double,
): MobAbility {

    override fun MobBuilder.apply(ctx: MobContext) {
        component(
            FireworkCreeper(
                count,
                damage
            )
        )
    }

}