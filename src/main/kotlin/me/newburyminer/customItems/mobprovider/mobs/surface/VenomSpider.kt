package me.newburyminer.customItems.mobprovider.mobs.surface

import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.LeapSlamAbility
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType

object VenomSpider: MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.SURFACE
    override val tier: MobTier = MobTier.STANDARD
    override val targetRange: Double = 50.0
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.SPIDER) {

        ability(
            MeleeEffectAbility(
                damage(linear(22.0 to 44.0, ctx), CustomDamageType.MELEE),
                attribute(Attribute.MOVEMENT_SPEED, -1.0, AttributeModifier.Operation.ADD_SCALAR, linear(10 to 20, ctx)),
                attribute(Attribute.JUMP_STRENGTH, -1.0, AttributeModifier.Operation.ADD_SCALAR, linear(10 to 20, ctx)),
                VanillaKnockbackApply()
            )
        )

        ability(
            LeapSlamAbility(
                linear(12.0 to 15.0, ctx),
                5.0,
                linear(3.0 to 4.0, ctx),
                linear(25.0 to 50.0, ctx),
                0.5,
                linear(200 to 180, ctx),
                linear(30 to 20, ctx),
                ParticleTheme.SURFACE
            )
        )

        health(
            linear(65.0 to 130.0, ctx)
        )

        movementSpeed(
            linear(1.2 to 1.5, ctx)
        )

    }

}