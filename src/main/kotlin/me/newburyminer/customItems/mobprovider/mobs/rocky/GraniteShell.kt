package me.newburyminer.customItems.mobprovider.mobs.rocky

import me.newburyminer.customItems.entity.hiteffects.effect.CustomKnockbackApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.BeamShooterAbility
import me.newburyminer.customItems.mobprovider.ability.spell.DeathSummonAbility
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType
import org.bukkit.util.Vector

object GraniteShell : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.ROCKY
	override val tier: MobTier = MobTier.ELITE
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.IRON_GOLEM) {

        ability(
            DeathSummonAbility(
                InfestedGeologist,
                1
            )
        )

        ability(
            MeleeEffectAbility(
                damage(linear(35.0 to 70.0, ctx), CustomDamageType.MELEE_NO_CD),
                CustomKnockbackApply(Vector(0.4, 1.0, 0.4))
            )
        )

        ability(
            BeamShooterAbility(
                linear(20.0 to 30.0, ctx),
                true,
                linear(60 to 40, ctx),
                linear(200 to 160, ctx),
                ParticleTheme.ROCKY,
                damage(linear(25.0 to 45.0, ctx), CustomDamageType.MAGIC_NO_CD),
                VanillaKnockbackApply(0.8),
                attribute(Attribute.GRAVITY, 0.5, AttributeModifier.Operation.ADD_SCALAR, linear(40 to 60, ctx))
            )
        )


        health(
            linear(200.0 to 400.0, ctx)
        )

        movementSpeed(
            linear(0.6 to 0.8, ctx)
        )

    }

}