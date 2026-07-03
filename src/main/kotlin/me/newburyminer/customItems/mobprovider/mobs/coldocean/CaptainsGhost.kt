package me.newburyminer.customItems.mobprovider.mobs.coldocean

import me.newburyminer.customItems.entity.components.spells.SummonerSpellComponent
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.ability.defensive.DamageShieldAbility
import me.newburyminer.customItems.mobprovider.ability.spell.BasicSlashAbility
import me.newburyminer.customItems.mobprovider.ability.spell.SummonerAbility
import org.bukkit.entity.EntityType

object CaptainsGhost: MobDefinition {

    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.EVOKER) {

        ability(
            SummonerAbility(
                linear(2 to 5, ctx),
                CrazedPrisoner,
                linear(80 to 50, ctx),
                linear(400 to 200, ctx),
                ParticleTheme.COLD_OCEAN
            )
        )

        ability(
            DamageShieldAbility(
                linear(1 to 4, ctx),
                linear(80 to 40, ctx),
                ParticleTheme.COLD_OCEAN
            )
        )

        ability(
            BasicSlashAbility(
                linear(2 to 4, ctx),
                linear(25.0 to 45.0, ctx),
                linear(150 to 100, ctx),
                ParticleTheme.COLD_OCEAN
            )
        )

        health(
            linear(150.0 to 300.0, ctx)
        )

        movementSpeed(
            linear(1.5 to 2.0, ctx)
        )

    }

}