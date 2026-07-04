package me.newburyminer.customItems.mobprovider.mobs.coldocean

import me.newburyminer.customItems.entity.components.spells.HealthThresholdComponent
import me.newburyminer.customItems.entity.components.spells.MultiMissileShooterComponent
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.CustomDamageApply
import me.newburyminer.customItems.entity.hiteffects.effect.CustomKnockbackApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaEffectApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.entity.velocity.StoppedStartVelocity
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.HomingSystem
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.BasicSlashAbility
import me.newburyminer.customItems.mobprovider.ability.spell.MultiMissileAbility
import me.newburyminer.customItems.mobprovider.ability.spell.SummonerAbility
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector

object PirateCaptain : MobDefinition() {

	override val tier: MobTier = MobTier.MINIBOSS
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.WITHER_SKELETON) {

        ability(
            MeleeEffectAbility(
                damage(linear(30.0 to 60.0, ctx), CustomDamageType.MELEE_NO_CD),
                VanillaEffectApply(PotionEffectType.WITHER, linear(60 to 180, ctx), 1),
                VanillaKnockbackApply()
            )
        )

        ability(
            SummonerAbility(
                linear(4 to 8, ctx),
                HoppingExplosives,
                linear(30 to 20, ctx),
                linear(600 to 400, ctx),
                ParticleTheme.COLD_OCEAN
            )
        )

        ability(
            SummonerAbility(
                linear(4 to 8, ctx),
                Buccaneer,
                linear(30 to 20, ctx),
                linear(600 to 400, ctx),
                ParticleTheme.COLD_OCEAN
            )
        )

        ability(
            SummonerAbility(
                linear(2 to 4, ctx),
                PirateMachineGunner,
                linear(30 to 20, ctx),
                linear(600 to 400, ctx),
                ParticleTheme.COLD_OCEAN
            )
        )

        ability(
            BasicSlashAbility(
                linear(3 to 4, ctx),
                linear(24.0 to 48.0, ctx),
                linear(100 to 70, ctx),
                ParticleTheme.COLD_OCEAN
            )
        )

        ability(
            MultiMissileAbility(
                linear(10.0 to 15.0, ctx),
                linear(2 to 4, ctx),
                linear(25.0 to 50.0, ctx),
                CustomKnockbackApply(Vector(-0.8, 0.5, -0.8)),
                linear(0.8 to 1.2, ctx),
                linear(30 to 20, ctx),
                linear(200 to 140, ctx),
                ParticleTheme.COLD_OCEAN
            )
        )

        component(
            HealthThresholdComponent(
                HitEffects(attribute(Attribute.MOVEMENT_SPEED, 0.5, AttributeModifier.Operation.ADD_SCALAR, 10000))
            )
        )

        health(
            linear(800.0 to 1600.0, ctx)
        )

        movementSpeed(
            linear(1.1 to 1.3, ctx)
        )

        scale(
            1.2
        )

    }

}