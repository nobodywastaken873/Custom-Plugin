package me.newburyminer.customItems.mobprovider.mobs.military

import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType

object SneakingCreaking: MobDefinition {

    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.CREAKING) {

        ability(
            MeleeEffectAbility(
                damage(linear(30.0 to 60.0, ctx), CustomDamageType.MELEE_NO_CD),
                attribute(Attribute.SCALE, -0.8, AttributeModifier.Operation.ADD_SCALAR, linear(20 to 40, ctx)),
                VanillaKnockbackApply()
            )
        )

        health(
            linear(90.0 to 180.0, ctx)
        )

        movementSpeed(
            linear(0.7 to 0.9, ctx)
        )

        scale(0.2)

    }

}