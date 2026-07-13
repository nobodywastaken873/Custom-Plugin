package me.newburyminer.customItems.mobprovider.mobs.military

import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.creeper.ArrowBombAbility
import me.newburyminer.customItems.mobprovider.ability.creeper.CustomExplosionAbility
import me.newburyminer.customItems.mobprovider.ability.spell.EffectAuraAbility
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType

object TrappingGrenade : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.MILITARY
	override val tier: MobTier = MobTier.STANDARD
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.CREEPER) {

        ability(
            ArrowBombAbility(
                linear(30 to 60, ctx),
                linear(14.0 to 28.0, ctx),
            )
        )

        ability(
            CustomExplosionAbility(
                -1.0,
                false
            )
        )

        ability(
            EffectAuraAbility(
                linear(2.0 to 3.0, ctx),
                1.0,
                ParticleTheme.MILITARY,
                20,
                attribute(Attribute.MOVEMENT_SPEED, -1.0, AttributeModifier.Operation.MULTIPLY_SCALAR_1, 20),
                attribute(Attribute.MOVEMENT_SPEED, -1.0, AttributeModifier.Operation.MULTIPLY_SCALAR_1, 20),
            )
        )

        health(
            linear(45.0 to 90.0, ctx)
        )

        movementSpeed(
            linear(0.9 to 1.3, ctx)
        )

    }

}