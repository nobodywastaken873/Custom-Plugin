package me.newburyminer.customItems.mobprovider.mobs.mystic

import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.EffectMissileAbility
import me.newburyminer.customItems.mobprovider.ability.spell.TrackingBeamAbility
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType

object MudGolem : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.MYSTIC
	override val tier: MobTier = MobTier.ELITE
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.IRON_GOLEM) {

        ability(
            TrackingBeamAbility(
                linear(25.0 to 35.0, ctx),
                10,
                200,
                200,
                ParticleTheme.MYSTIC,
                damage(linear(10.0 to 20.0, ctx), CustomDamageType.MELEE_NO_CD)
            )
        )

        ability(
            EffectMissileAbility(
                linear(20.0 to 30.0, ctx),
                0.05,
                linear(1.5 to 2.5, ctx),
                linear(40 to 30, ctx),
                linear(150 to 100, ctx),
                ParticleTheme.MYSTIC,
                attribute(
                    Attribute.ARMOR,
                    -linear(3.0 to 6.0, ctx),
                    AttributeModifier.Operation.ADD_NUMBER,
                    linear(150 to 200, ctx)
                )
            )
        )

        ability(
            MeleeEffectAbility(
                damage(linear(26.0 to 52.0, ctx), CustomDamageType.MELEE_NO_CD),
                VanillaKnockbackApply()
            )
        )

        health(
            linear(260.0 to 520.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.2, ctx)
        )

    }

}