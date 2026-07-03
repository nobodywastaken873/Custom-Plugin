package me.newburyminer.customItems.mobprovider.ability.creeper

import me.newburyminer.customItems.entity.components.creepers.CustomExplosionCreeper
import me.newburyminer.customItems.entity.components.creepers.FirebombCreeper
import me.newburyminer.customItems.mobprovider.MobAbility
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext

class CustomExplosionAbility(
    val power: Double,
    val setFire: Boolean,
    val breakBlocks: Boolean = true,
    val setLavaRate: Double = 0.0,
): MobAbility {

    override fun MobBuilder.apply(ctx: MobContext) {
        component(
            CustomExplosionCreeper(
                power.toFloat(),
                setFire,
                breakBlocks
            )
        )

        if (setLavaRate > 0.0) {
            component(
                FirebombCreeper(
                    setLavaRate
                )
            )
        }
    }

}