package me.newburyminer.customItems.mobprovider.ability.defensive

import me.newburyminer.customItems.entity.components.defensive.BasicDodgeComponent
import me.newburyminer.customItems.mobprovider.MobAbility
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext

class BasicDodgeAbility(
    val dodgeRate: Double,
): MobAbility {

    override fun MobBuilder.applyAbility(ctx: MobContext) {
        component(
            BasicDodgeComponent(
                dodgeRate
            )
        )
    }

}