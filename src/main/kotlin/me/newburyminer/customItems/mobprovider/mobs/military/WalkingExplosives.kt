package me.newburyminer.customItems.mobprovider.mobs.military

import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.ability.creeper.CustomExplosionAbility
import me.newburyminer.customItems.mobprovider.ability.creeper.PreIgniteAbility
import org.bukkit.entity.EntityType

object WalkingExplosives: MobDefinition {

    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.CREEPER) {

        ability(
            PreIgniteAbility(
                linear(7.0 to 9.0, ctx),
            )
        )

        ability(
            CustomExplosionAbility(
                linear(3.5 to 7.0, ctx),
                true
            )
        )

        health(
            linear(35.0 to 70.0, ctx)
        )

        movementSpeed(
            linear(1.2 to 1.4, ctx)
        )

    }

}