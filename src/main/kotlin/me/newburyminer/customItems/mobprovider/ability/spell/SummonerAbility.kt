package me.newburyminer.customItems.mobprovider.ability.spell

import me.newburyminer.customItems.entity.components.spells.SpellCasterComponent
import me.newburyminer.customItems.entity.components.spells.SummonerSpellComponent
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobAbility
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition

class SummonerAbility(
    val count: Int,
    val mob: MobDefinition,
    val castDuration: Int,
    val cooldown: Int,
    val particleTheme: ParticleTheme
): MobAbility {

    override fun MobBuilder.applyAbility(ctx: MobContext) {

        component(
            SummonerSpellComponent(
                count,
                mob,
                castDuration,
                cooldown,
                particleTheme
            )
        )

        component(
            SpellCasterComponent()
        )

    }

}