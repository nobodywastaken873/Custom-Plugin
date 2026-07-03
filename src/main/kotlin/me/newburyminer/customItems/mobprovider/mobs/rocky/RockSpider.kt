package me.newburyminer.customItems.mobprovider.mobs.rocky

import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier.Operation
import org.bukkit.entity.EntityType

object RockSpider: MobDefinition {

    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.SPIDER) {

        ability(
            MeleeEffectAbility(
                damage(linear(22.0 to 44.0, ctx), CustomDamageType.MELEE_NO_CD),
                attribute(Attribute.ARMOR_TOUGHNESS, -0.25, Operation.ADD_NUMBER, linear(80 to 120, ctx)),
                VanillaKnockbackApply()
            )
        )

        health(
            linear(25.0 to 60.0, ctx)
        )

        movementSpeed(
            linear(1.2 to 1.4, ctx)
        )

    }

}