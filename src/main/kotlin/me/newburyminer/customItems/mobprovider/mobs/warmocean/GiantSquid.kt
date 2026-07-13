package me.newburyminer.customItems.mobprovider.mobs.warmocean

import me.newburyminer.customItems.entity.hiteffects.effect.CustomKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.spell.EffectAuraAbility
import org.bukkit.entity.EntityType
import org.bukkit.util.Vector

object GiantSquid : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.WARM_OCEAN
	override val tier: MobTier = MobTier.ELITE
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.GLOW_SQUID) {

        ability(
            EffectAuraAbility(
                linear(5.0 to 7.0, ctx),
                2.0,
                ParticleTheme.WARM_OCEAN,
                5,
                CustomKnockbackApply(Vector(-1.0, 0.5, -1.0))
            )
        )

        ability(
            EffectAuraAbility(
                linear(2.5 to 3.5, ctx),
                2.0,
                ParticleTheme.WARM_OCEAN,
                5,
                damage(linear(20.0 to 40.0, ctx), CustomDamageType.MELEE_NO_CD)
            )
        )

        health(
            linear(400.0 to 800.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.5, ctx)
        )

        scale(3.5)

    }

}