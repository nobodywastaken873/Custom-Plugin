package me.newburyminer.customItems.mobprovider.mobs.military

import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.ability.defensive.LeapDodgeAbility
import me.newburyminer.customItems.mobprovider.ability.spell.BasicSlashAbility
import me.newburyminer.customItems.mobprovider.ability.spell.DeathSummonAbility
import me.newburyminer.customItems.mobprovider.ability.spell.SummonerAbility
import org.bukkit.entity.EntityType

object DroneSwarmer: MobDefinition {

    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.EVOKER) {

        ability(
            DeathSummonAbility(
                FPVDrone,
                linear(3 to 5, ctx),
            )
        )

        ability(
            SummonerAbility(
                linear(4 to 8, ctx),
                FPVDrone,
                linear(30 to 20, ctx),
                linear(200 to 150, ctx),
                ParticleTheme.MILITARY
            )
        )

        ability(
            BasicSlashAbility(
                linear(2 to 4, ctx),
                linear(24.0 to 48.0, ctx),
                linear(100 to 80, ctx),
                ParticleTheme.MILITARY
            )
        )

        ability(
            LeapDodgeAbility(
                linear(0.25 to 0.5, ctx),
                linear(100 to 80, ctx),
                1.2
            )
        )

        health(
            linear(280.0 to 560.0, ctx)
        )

        movementSpeed(
            linear(1.4 to 1.8, ctx)
        )

    }

}