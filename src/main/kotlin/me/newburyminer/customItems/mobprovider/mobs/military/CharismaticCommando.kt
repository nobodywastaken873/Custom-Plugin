package me.newburyminer.customItems.mobprovider.mobs.military

import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.ability.defensive.LeapDodgeAbility
import me.newburyminer.customItems.mobprovider.ability.spell.SummonerAbility
import org.bukkit.entity.EntityType

object CharismaticCommando: MobDefinition {

    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.EVOKER) {

        ability(
            SummonerAbility(
                linear(2 to 4, ctx),
                Infantryman,
                linear(60 to 50, ctx),
                linear(400 to 350, ctx),
                ParticleTheme.MILITARY
            )
        )

        ability(
            SummonerAbility(
                linear(2 to 4, ctx),
                AttackHound,
                linear(60 to 50, ctx),
                linear(400 to 350, ctx),
                ParticleTheme.MILITARY
            )
        )

        ability(
            SummonerAbility(
                linear(1 to 2, ctx),
                MachineGunFortification,
                linear(60 to 50, ctx),
                linear(400 to 350, ctx),
                ParticleTheme.MILITARY
            )
        )

        ability(
            SummonerAbility(
                linear(1 to 2, ctx),
                DroneSwarmer,
                linear(60 to 50, ctx),
                linear(400 to 350, ctx),
                ParticleTheme.MILITARY
            )
        )

        ability(
            LeapDodgeAbility(
                linear(0.2 to 0.4, ctx),
                linear(150 to 100, ctx),
                1.5
            )
        )

        health(
            linear(350.0 to 700.0, ctx)
        )

        movementSpeed(
            linear(1.4 to 1.7, ctx)
        )

    }

}