package me.newburyminer.customItems.mobprovider.mobs.mystic

import me.newburyminer.customItems.entity.hiteffects.effect.DisableShieldApply
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.creeper.CustomExplosionAbility
import me.newburyminer.customItems.mobprovider.ability.creeper.PreIgniteAbility
import me.newburyminer.customItems.mobprovider.ability.spell.EffectMissileAbility
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType

object AxedCreeper : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.MYSTIC
	override val tier: MobTier = MobTier.STANDARD
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.CREEPER) {

        ability(
            PreIgniteAbility(
                linear(8.0 to 10.0, ctx),
            )
        )

        ability(
            CustomExplosionAbility(
                linear(2.8 to 5.6, ctx),
                false
            )
        )

        ability(
            EffectMissileAbility(
                linear(20.0 to 30.0, ctx),
                0.05,
                2.5,
                linear(40 to 30, ctx),
                linear(150 to 100, ctx),
                ParticleTheme.MYSTIC,
                DisableShieldApply(true)
            )
        )

        health(
            linear(50.0 to 100.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.2, ctx)
        )

    }

}