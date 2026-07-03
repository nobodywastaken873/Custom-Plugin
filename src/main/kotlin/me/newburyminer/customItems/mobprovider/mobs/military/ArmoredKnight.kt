package me.newburyminer.customItems.mobprovider.mobs.military

import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.defensive.DamageShieldAbility
import org.bukkit.entity.EntityType

object ArmoredKnight: MobDefinition {

    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.VINDICATOR) {

        ability(
            MeleeEffectAbility(
                damage(linear(20.0 to 40.0, ctx), CustomDamageType.MELEE_NO_CD),
                VanillaKnockbackApply()
            )
        )

        ability(
            DamageShieldAbility(
                linear(3 to 5, ctx),
                linear(200 to 150, ctx),
                ParticleTheme.MILITARY
            )
        )

        health(
            linear(150.0 to 300.0, ctx)
        )

        movementSpeed(
            linear(0.6 to 0.8, ctx)
        )

    }

}