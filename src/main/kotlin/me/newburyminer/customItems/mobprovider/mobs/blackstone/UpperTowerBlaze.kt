package me.newburyminer.customItems.mobprovider.mobs.blackstone

import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.MagicMissileApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.entity.velocity.StoppedStartVelocity
import me.newburyminer.customItems.entity.velocity.VelocityProvider
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.HomingSystem
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.ability.spell.BeamShooterAbility
import me.newburyminer.customItems.mobprovider.ability.spell.EffectMissileAbility
import me.newburyminer.customItems.mobprovider.ability.spell.SummonerAbility
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType

object UpperTowerBlaze: MobDefinition {

    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.BLAZE) {

        ability(
            SummonerAbility(
                linear(2 to 4, ctx),
                DarkDuelist,
                linear(40 to 30, ctx),
                linear(400 to 350, ctx),
                ParticleTheme.BLACKSTONE
            )
        )

        ability(
            SummonerAbility(
                linear(2 to 4, ctx),
                FireBombardier,
                linear(40 to 30, ctx),
                linear(400 to 350, ctx),
                ParticleTheme.BLACKSTONE
            )
        )

        ability(
            BeamShooterAbility(
                linear(25.0 to 40.0, ctx),
                false,
                linear(30 to 15, ctx),
                linear(40 to 30, ctx),
                ParticleTheme.BLACKSTONE,
                damage(linear(30.0 to 60.0, ctx), CustomDamageType.PROJECTILE_NO_CD),
                VanillaKnockbackApply()
            )
        )

        ability(
            EffectMissileAbility(
                linear(30.0 to 40.0, ctx),
                0.05,
                linear(2.0 to 3.0, ctx),
                linear(30 to 20, ctx),
                linear(70 to 40, ctx),
                ParticleTheme.BLACKSTONE,
                MagicMissileApply(
                    linear(3 to 5, ctx),
                    0.05,
                    StoppedStartVelocity(0.2, 0.05, HomingSystem.Type.BOTH_SCALED, 40, 2.0),
                    HitEffects(damage(linear(20.0 to 40.0, ctx), CustomDamageType.PROJECTILE_NO_CD)),
                    ParticleTheme.BLACKSTONE,
                )
            )
        )

        health(
            linear(500.0 to 1000.0, ctx)
        )

        movementSpeed(
            linear(0.8 to 1.2, ctx)
        )

        scale(1.2)

    }

}