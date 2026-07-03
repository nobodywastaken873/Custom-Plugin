package me.newburyminer.customItems.mobprovider.mobs.desert

import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.ability.creeper.ArrowBombAbility
import me.newburyminer.customItems.mobprovider.ability.creeper.CustomExplosionAbility
import org.bukkit.entity.EntityType

object PinbagCreeper: MobDefinition {

    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.CREEPER) {

        ability(
            CustomExplosionAbility(-1.0, false)
        )

        ability(
            ArrowBombAbility(
                linear(50 to 100, ctx),
                linear(15.0 to 30.0, ctx)
            )
        )

        health(
            linear(40.0 to 80.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.4, ctx)
        )

    }

}