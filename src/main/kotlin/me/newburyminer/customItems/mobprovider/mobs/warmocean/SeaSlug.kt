package me.newburyminer.customItems.mobprovider.mobs.warmocean

import me.newburyminer.customItems.entity.hiteffects.effect.VanillaEffectApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.EffectAuraAbility
import org.bukkit.entity.EntityType
import org.bukkit.potion.PotionEffectType

object SeaSlug : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.WARM_OCEAN
	override val tier: MobTier = MobTier.GRUNT
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.ENDERMITE) {

        ability(
            MeleeEffectAbility(
                VanillaEffectApply(PotionEffectType.POISON, linear(40 to 60, ctx), linear(3 to 4, ctx))
            )
        )

        ability(
            EffectAuraAbility(
                linear(1.0 to 2.0, ctx),
                1.0,
                ParticleTheme.WARM_OCEAN,
                20,
                VanillaEffectApply(PotionEffectType.MINING_FATIGUE, linear(50 to 100, ctx), 0)
            )
        )

        health(
            linear(25.0 to 50.0, ctx)
        )

        movementSpeed(
            linear(0.9 to 1.2, ctx)
        )

    }

}