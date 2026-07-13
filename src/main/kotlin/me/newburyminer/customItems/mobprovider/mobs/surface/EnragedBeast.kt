package me.newburyminer.customItems.mobprovider.mobs.surface

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
import me.newburyminer.customItems.mobprovider.ability.defensive.DamageShieldAbility
import me.newburyminer.customItems.mobprovider.ability.spell.EffectAuraAbility
import org.bukkit.entity.EntityType
import org.bukkit.potion.PotionEffectType

object EnragedBeast: MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.SURFACE
    override val targetRange: Double = 60.0
    override val tier: MobTier = MobTier.ELITE
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.ZOGLIN) {

        ability(
            MeleeEffectAbility(
                damage(linear(28.0 to 56.0, ctx), CustomDamageType.MELEE_NO_CD),
                VanillaKnockbackApply()
            )
        )

        ability(
            EffectAuraAbility(
                linear(3.5 to 4.5, ctx),
                1.0,
                ParticleTheme.SURFACE,
                10,
                damage(linear(10.0 to 20.0, ctx), CustomDamageType.MELEE_NO_CD),
                VanillaEffectApply(PotionEffectType.POISON, linear(200 to 300, ctx), linear(1 to 2, ctx)),
                VanillaEffectApply(PotionEffectType.MINING_FATIGUE, linear(200 to 300, ctx), linear(0 to 1, ctx))
            )
        )

        ability(
            DamageShieldAbility(
                linear(1 to 2, ctx),
                linear(80 to 60, ctx),
                ParticleTheme.SURFACE,
            )
        )

        health(
            linear(240.0 to 480.0, ctx)
        )

        movementSpeed(
            linear(1.4 to 1.8, ctx)
        )

        scale(
            1.7
        )

    }

}