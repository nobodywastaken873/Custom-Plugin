package me.newburyminer.customItems.mobprovider.mobs.bosses.warden

import me.newburyminer.customItems.entity.hiteffects.effect.ProjectileKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.projectile.ProjectileEffectAbility
import org.bukkit.entity.EntityType

object WardenSkeleton: MobDefinition() {

    override val tier: MobTier = MobTier.GRUNT
    override val targetRange: Double = 50.0
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.SKELETON) {

        ability(
            ProjectileEffectAbility(
                damage(linear(22.0 to 44.0, ctx), CustomDamageType.PROJECTILE),
                ProjectileKnockbackApply()
            )
        )

        health(
            linear(20.0 to 50.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.2, ctx)
        )

    }

}