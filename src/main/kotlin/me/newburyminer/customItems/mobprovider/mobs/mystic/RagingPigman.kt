package me.newburyminer.customItems.mobprovider.mobs.mystic

import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType

object RagingPigman: MobDefinition {

    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.ZOMBIFIED_PIGLIN) {

        ability(
            MeleeEffectAbility(
                damage(linear(24.0 to 48.0, ctx), CustomDamageType.MELEE_NO_CD),
                VanillaKnockbackApply()
            )
        )

        health(
            linear(30.0 to 60.0, ctx)
        )

        movementSpeed(
            linear(1.3 to 1.6, ctx)
        )

        attribute(
            Attribute.KNOCKBACK_RESISTANCE, 1.0, AttributeModifier.Operation.ADD_NUMBER
        )

    }

}