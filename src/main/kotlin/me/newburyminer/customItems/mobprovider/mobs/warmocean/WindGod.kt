package me.newburyminer.customItems.mobprovider.mobs.warmocean

import me.newburyminer.customItems.entity.hiteffects.effect.CustomKnockbackApply
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.spell.TrackingBeamAbility
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType
import org.bukkit.util.Vector

object WindGod : MobDefinition() {

	override val tier: MobTier = MobTier.ELITE
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.BREEZE) {

        ability(
            TrackingBeamAbility(
                linear(20.0 to 25.0, ctx),
                linear(10 to 8, ctx),
                500,
                10,
                ParticleTheme.WARM_OCEAN,
                CustomKnockbackApply(Vector(0, 3, 0)),
                attribute(
                    Attribute.FALL_DAMAGE_MULTIPLIER,
                    linear(4.0 to 8.0, ctx),
                    AttributeModifier.Operation.ADD_NUMBER,
                    linear(40 to 80, ctx),
                )
            )
        )

        health(
            linear(160.0 to 320.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.2, ctx)
        )

        apply {
            val top = UpperWindGod.build(ctx).createEntity(ctx)
            this.addPassenger(top)
        }

    }

}