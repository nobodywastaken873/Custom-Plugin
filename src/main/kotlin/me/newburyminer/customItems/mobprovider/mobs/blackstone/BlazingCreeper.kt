package me.newburyminer.customItems.mobprovider.mobs.blackstone

import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.creeper.CustomExplosionAbility
import me.newburyminer.customItems.mobprovider.ability.creeper.PreIgniteAbility
import org.bukkit.entity.EntityType

object BlazingCreeper : MobDefinition() {

	override val tier: MobTier = MobTier.GRUNT
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.CREEPER) {

        ability(
            PreIgniteAbility(linear(6.0 to 10.0, ctx))
        )

        ability(
            CustomExplosionAbility(
                linear(3.5 to 7.0, ctx),
                true
            )
        )

        health(
            linear(30.0 to 60.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.5, ctx)
        )

        scale(0.85)

    }

}