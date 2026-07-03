package me.newburyminer.customItems.mobprovider.ability.defensive

import me.newburyminer.customItems.entity.components.defensive.DamageBlockerComponent
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobAbility
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext

class DamageShieldAbility(
    val shieldCount: Int,
    val regenRate: Int,
    val particleTheme: ParticleTheme
): MobAbility {

    override fun MobBuilder.apply(ctx: MobContext) {
        component(
            DamageBlockerComponent(
                shieldCount,
                regenRate,
                particleTheme
            )
        )
    }

}