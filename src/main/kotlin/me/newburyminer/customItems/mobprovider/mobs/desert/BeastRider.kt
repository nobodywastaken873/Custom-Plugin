package me.newburyminer.customItems.mobprovider.mobs.desert

import me.newburyminer.customItems.entity.hiteffects.effect.CustomKnockbackApply
import me.newburyminer.customItems.entity.hiteffects.effect.ProjectileKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobFactory
import me.newburyminer.customItems.mobprovider.ability.projectile.ProjectileEffectAbility
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType
import org.bukkit.util.Vector

object BeastRider: MobDefinition {

    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.PARCHED) {

        ability(
            ProjectileEffectAbility(
                damage(linear(25.0 to 50.0, ctx), CustomDamageType.PROJECTILE_NO_CD),
                CustomKnockbackApply(Vector(0.4, 2.0, 0.4)),
                attribute(Attribute.FALL_DAMAGE_MULTIPLIER, linear(4.0 to 10.0, ctx), AttributeModifier.Operation.ADD_SCALAR, linear(40 to 80, ctx))
            )
        )

        health(
            linear(60.0 to 120.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.2, ctx)
        )

    }

}