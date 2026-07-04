package me.newburyminer.customItems.mobprovider.mobs.military

import me.newburyminer.customItems.entity.components.spells.LeapComponent
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import org.bukkit.entity.EntityType

object TrustySteed : MobDefinition() {

	override val tier: MobTier = MobTier.STANDARD
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.ZOMBIE_HORSE) {

        component(
            LeapComponent(
                linear(12.0 to 14.0, ctx),
                0.8,
                linear(200 to 180, ctx),
            )
        )

        health(
            linear(120.0 to 240.0, ctx)
        )

        movementSpeed(
            linear(2.0 to 2.5, ctx)
        )

        apply {
            val top = JoustingKnight.build(ctx).createEntity(ctx)
            this.addPassenger(top)
        }

    }

}