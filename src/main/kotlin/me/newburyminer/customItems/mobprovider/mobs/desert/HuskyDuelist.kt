package me.newburyminer.customItems.mobprovider.mobs.desert

import me.newburyminer.customItems.entity.components.spells.TeleportBehindComponent
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.defensive.DamageShieldAbility
import me.newburyminer.customItems.mobprovider.ability.spell.TeleportBehindAbility
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType

object HuskyDuelist : MobDefinition() {

	override val tier: MobTier = MobTier.ELITE
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.HUSK) {

        ability(
            TeleportBehindAbility(
                linear(20.0 to 40.0, ctx),
                linear(400 to 300, ctx),
                linear(40 to 30, ctx)
            )
        )

        ability(
            MeleeEffectAbility(
                damage(linear(30.0 to 60.0, ctx), CustomDamageType.MELEE_NO_CD),
                attribute(Attribute.GRAVITY, 2.0, AttributeModifier.Operation.ADD_SCALAR, linear(20 to 40, ctx)),
                VanillaKnockbackApply()
            )
        )

        ability(
            DamageShieldAbility(
                linear(2 to 4, ctx),
                linear(40 to 30, ctx),
                ParticleTheme.DESERT
            )
        )

        health(
            linear(160.0 to 320.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.4, ctx)
        )

    }

}