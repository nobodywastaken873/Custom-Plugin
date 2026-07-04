package me.newburyminer.customItems.mobprovider.ability.spell

import me.newburyminer.customItems.entity.components.spells.SpellCasterComponent
import me.newburyminer.customItems.entity.components.spells.TeleportBehindComponent
import me.newburyminer.customItems.mobprovider.MobAbility
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext

class TeleportBehindAbility(
    val range: Double,
    val cooldown: Int,
    val castTime: Int,
): MobAbility {

    override fun MobBuilder.applyAbility(ctx: MobContext) {

        component(
            TeleportBehindComponent(
                range,
                castTime,
                cooldown,
            )
        )

        component(
            SpellCasterComponent(
                -0.2
            )
        )

    }

}