package me.newburyminer.customItems.mobprovider.mobs.warmocean

import me.newburyminer.customItems.entity.hiteffects.effect.VanillaEffectApply
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.creeper.CreeperHopAbility
import me.newburyminer.customItems.mobprovider.ability.creeper.CustomExplosionAbility
import me.newburyminer.customItems.mobprovider.ability.spell.EffectAuraAbility
import org.bukkit.entity.EntityType
import org.bukkit.potion.PotionEffectType

object ExplosiveCoral : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.WARM_OCEAN
	override val tier: MobTier = MobTier.STANDARD
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.CREEPER) {

        ability(
            CustomExplosionAbility(
                linear(2.8 to 5.6, ctx),
                false
            )
        )

        ability(
            CreeperHopAbility()
        )

        ability(
            EffectAuraAbility(
                linear(3.5 to 5.0, ctx),
                1.0,
                ParticleTheme.WARM_OCEAN,
                20,
                VanillaEffectApply(PotionEffectType.POISON, linear(60 to 120, ctx), linear(1 to 2, ctx))
            )
        )

        health(
            linear(60.0 to 120.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.5, ctx)
        )

    }

}