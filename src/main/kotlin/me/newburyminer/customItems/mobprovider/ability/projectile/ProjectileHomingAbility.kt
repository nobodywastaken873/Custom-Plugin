package me.newburyminer.customItems.mobprovider.ability.projectile

import me.newburyminer.customItems.entity.components.melee.MeleeCustomHit
import me.newburyminer.customItems.entity.components.projectileshooters.HomingProjectileShooter
import me.newburyminer.customItems.entity.components.projectileshooters.ProjectileDamageShooter
import me.newburyminer.customItems.entity.hiteffects.HitEffect
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.helpers.HomingSystem
import me.newburyminer.customItems.mobprovider.MobAbility
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext

class ProjectileHomingAbility(
    val turnRate: Double = 0.08,
    val homingType: HomingSystem.Type = HomingSystem.Type.BOTH_SCALED
): MobAbility {

    override fun MobBuilder.applyAbility(ctx: MobContext) {
        component(
            HomingProjectileShooter(
                turnRate,
                homingType,
            )
        )
    }

}