package me.newburyminer.customItems.mobprovider.mobs.surface

import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.creeper.CustomExplosionAbility
import me.newburyminer.customItems.mobprovider.ability.creeper.PreIgniteAbility
import org.bukkit.entity.EntityType

object LeapingCreeper: MobDefinition() {

    override val tier: MobTier = MobTier.GRUNT
    override val targetRange: Double = 65.0
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.CREEPER) {

        ability(
            PreIgniteAbility(
                linear(14.0 to 20.0, ctx),
            )
        )

        ability(
            CustomExplosionAbility(
                linear(3.2 to 7.0, ctx),
                true,
            )
        )

        health(
            linear(30.0 to 60.0, ctx)
        )

        movementSpeed(
            linear(1.4 to 1.7, ctx)
        )

    }

}