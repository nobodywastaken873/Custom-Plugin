package me.newburyminer.customItems.mobprovider.ability.defensive

import me.newburyminer.customItems.entity.components.defensive.LeapDodgeComponent
import me.newburyminer.customItems.mobprovider.MobAbility
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext

class LeapDodgeAbility(
    val dodgeRate: Double,
    val baseCooldown: Int,
    val jumpStrength: Double
): MobAbility {

    override fun MobBuilder.applyAbility(ctx: MobContext) {
        component(
            LeapDodgeComponent(
                dodgeRate,
                baseCooldown,
                jumpStrength
            )
        )
    }

}