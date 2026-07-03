package me.newburyminer.customItems.mobprovider.mobs.rocky

import me.newburyminer.customItems.entity.components.spells.HealthThresholdComponent
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.MagicMissileApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.entity.velocity.StoppedStartVelocity
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.HomingSystem
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import org.bukkit.entity.EntityType

object RockGolem: MobDefinition {

    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.ZOMBIE) {

        ability(
            MeleeEffectAbility(
                damage(linear(30.0 to 60.0, ctx), CustomDamageType.MELEE_NO_CD),
                VanillaKnockbackApply()
            )
        )

        component(
            HealthThresholdComponent(
                HitEffects(
                    MagicMissileApply(
                        linear(1 to 3, ctx),
                        0.0,
                        StoppedStartVelocity(0.1, 0.05, HomingSystem.Type.BOTH_SCALED, 20, 1.5),
                        HitEffects(damage(linear(30.0 to 60.0, ctx), CustomDamageType.PROJECTILE_NO_CD)),
                        ParticleTheme.ROCKY
                    )
                ),
                0.5
            )
        )

        health(
            linear(85.0 to 170.0, ctx)
        )

        movementSpeed(
            linear(0.7 to 0.9, ctx)
        )

    }

}