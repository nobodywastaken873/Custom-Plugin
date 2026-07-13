package me.newburyminer.customItems.mobprovider.mobs.rocky

import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.defensive.LeapDodgeAbility
import me.newburyminer.customItems.mobprovider.ability.spell.DamageAuraCastAbility
import me.newburyminer.customItems.mobprovider.ability.spell.EffectMissileAbility
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType

object Medusa : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.ROCKY
	override val tier: MobTier = MobTier.ELITE
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.EVOKER) {

        ability(
            DamageAuraCastAbility(
                linear(2.0 to 4.0, ctx),
                1.0,
                40,
                linear(40 to 30, ctx),
                linear(100 to 200, ctx),
                20,
                ParticleTheme.ROCKY,
                linear(40 to 20, ctx),
                linear(200 to 160, ctx),
                linear(15.0 to 25.0, ctx),
                attribute(
                    Attribute.MOVEMENT_SPEED,
                    -1.0,
                    AttributeModifier.Operation.MULTIPLY_SCALAR_1,
                    linear(20 to 30, ctx)
                ),
                attribute(
                    Attribute.JUMP_STRENGTH,
                    -1.0,
                    AttributeModifier.Operation.MULTIPLY_SCALAR_1,
                    linear(20 to 30, ctx)
                ),
            )
        )

        ability(
            EffectMissileAbility(
                linear(30.0 to 40.0, ctx),
                0.05,
                linear(2.0 to 3.0, ctx),
                linear(30 to 20, ctx),
                linear(70 to 40, ctx),
                ParticleTheme.ROCKY,
                damage(linear(15.0 to 30.0, ctx), CustomDamageType.MAGIC_NO_CD),
                VanillaKnockbackApply(0.1)
            )
        )

        ability(
            LeapDodgeAbility(
                linear(0.2 to 0.5, ctx),
                linear(200 to 100, ctx),
                1.5
            )
        )

        health(
            linear(160.0 to 320.0, ctx)
        )

        movementSpeed(
            linear(1.3 to 1.7, ctx)
        )

    }

}