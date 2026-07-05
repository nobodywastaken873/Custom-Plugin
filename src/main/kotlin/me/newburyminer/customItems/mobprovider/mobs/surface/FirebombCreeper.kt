package me.newburyminer.customItems.mobprovider.mobs.surface

import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.creeper.CreeperHopAbility
import me.newburyminer.customItems.mobprovider.ability.creeper.CustomExplosionAbility
import org.bukkit.entity.EntityType

object FirebombCreeper: MobDefinition() {

    override val tier: MobTier = MobTier.STANDARD
    override val targetRange: Double = 50.0
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.CREEPER) {

        ability(
            CreeperHopAbility()
        )

        ability(
            CustomExplosionAbility(
                linear(3.4 to 6.8, ctx),
                true,
                setLavaRate = linear(0.07 to 0.15, ctx),
            )
        )

        health(
            linear(50.0 to 100.0, ctx)
        )

        movementSpeed(
            linear(1.2 to 1.5, ctx)
        )

    }

}