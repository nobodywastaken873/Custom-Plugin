package me.newburyminer.customItems.mobprovider.mobs.military

import me.newburyminer.customItems.entity.components.spells.HealthThresholdComponent
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType

object PanickedSoldier: MobDefinition {

    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.VINDICATOR) {

        ability(
            MeleeEffectAbility(
                damage(linear(24.0 to 48.0, ctx), CustomDamageType.MELEE_NO_CD),
                VanillaKnockbackApply()
            )
        )

        component(
            HealthThresholdComponent(
                HitEffects(attribute(Attribute.MOVEMENT_SPEED, 1.0, AttributeModifier.Operation.ADD_SCALAR, 100000)),
                0.5
            )
        )

        health(
            linear(25.0 to 50.0, ctx)
        )

        movementSpeed(
            linear(1.3 to 1.5, ctx)
        )

    }

}