package me.newburyminer.customItems.mobprovider.mobs.coldocean

import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.ability.creeper.CreeperHopAbility
import me.newburyminer.customItems.mobprovider.ability.creeper.CustomExplosionAbility
import org.bukkit.entity.EntityType

object HoppingExplosives: MobDefinition {

    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.CREEPER) {

        ability(
            CreeperHopAbility()
        )

        ability(
            CustomExplosionAbility(
                linear(4.0 to 7.0, ctx),
                false
            )
        )

        health(
            linear(30.0 to 60.0, ctx)
        )

        movementSpeed(
            linear(1.2 to 1.4, ctx)
        )

    }

}