package me.newburyminer.customItems.mobprovider.mobs.mystic

import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.creeper.ArrowBombAbility
import me.newburyminer.customItems.mobprovider.ability.creeper.CustomExplosionAbility
import org.bukkit.entity.EntityType

object PorcupineCreeper : MobDefinition() {

	override val tier: MobTier = MobTier.GRUNT
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.CREEPER) {

        ability(
            ArrowBombAbility(
                linear(30 to 60, ctx),
                linear(8.0 to 13.0, ctx),
            )
        )

        ability(
            CustomExplosionAbility(
                -1.0,
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