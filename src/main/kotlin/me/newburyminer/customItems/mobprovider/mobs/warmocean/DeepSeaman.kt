package me.newburyminer.customItems.mobprovider.mobs.warmocean

import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.defensive.BasicDodgeAbility
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType

object DeepSeaman : MobDefinition() {

	override val tier: MobTier = MobTier.STANDARD
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.ENDERMAN) {

        ability(
            MeleeEffectAbility(
                damage(linear(32.0 to 64.0, ctx), CustomDamageType.MELEE_NO_CD),
                attribute(
                    Attribute.MOVEMENT_SPEED,
                    -0.1,
                    AttributeModifier.Operation.ADD_SCALAR,
                    linear(35 to 70, ctx)
                ),
                VanillaKnockbackApply()
            )
        )

        ability(
            BasicDodgeAbility(
                linear(0.3 to 0.6, ctx),
            )
        )

        health(
            linear(85.0 to 170.0, ctx)
        )

        movementSpeed(
            linear(1.3 to 1.6, ctx)
        )

    }

}