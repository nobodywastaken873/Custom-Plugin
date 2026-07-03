package me.newburyminer.customItems.mobprovider.mobs.blackstone

import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.ability.creeper.CustomExplosionAbility
import org.bukkit.entity.Creeper
import org.bukkit.entity.EntityType

object InertCreeper: MobDefinition {

    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.CREEPER) {

        ability(
            CustomExplosionAbility(
                linear(4.0 to 8.0, ctx),
                true
            )
        )

        health(
            linear(30.0 to 60.0, ctx)
        )

        movementSpeed(
            linear(0.9 to 1.1, ctx)
        )

        apply {
            if (this !is Creeper) return@apply
            this.maxFuseTicks = 400
        }

    }

}