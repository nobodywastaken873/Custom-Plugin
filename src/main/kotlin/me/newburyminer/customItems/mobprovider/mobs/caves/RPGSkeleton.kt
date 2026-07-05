package me.newburyminer.customItems.mobprovider.mobs.caves

import me.newburyminer.customItems.entity.hiteffects.effect.ExplosionApply
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.projectile.ProjectileEffectAbility
import org.bukkit.entity.EntityType

object RPGSkeleton: MobDefinition() {

    override val tier: MobTier = MobTier.STANDARD
    override val targetRange: Double = 55.0
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.SKELETON) {

        ability(
            ProjectileEffectAbility(
                ExplosionApply(
                    linear(3.0 to 6.0, ctx).toFloat(),
                    true
                )
            )
        )

        health(
            linear(50.0 to 100.0, ctx)
        )

        movementSpeed(
            linear(1.4 to 1.8, ctx)
        )

    }

}