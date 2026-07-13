package me.newburyminer.customItems.mobprovider.mobs.mystic

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

object PollenLadenBee : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.MYSTIC
	override val tier: MobTier = MobTier.GRUNT
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.BEE) {

        ability(
            MeleeEffectAbility(
                damage(linear(23.0 to 46.0, ctx), CustomDamageType.MELEE_NO_CD),
                VanillaEffectApply(PotionEffectType.POISON, linear(40 to 60, ctx), linear(1 to 2, ctx)),
                VanillaKnockbackApply()
            )
        )

        ability(
            EffectAuraAbility(
                linear(1.5 to 3.0, ctx),
                2.0,
                ParticleTheme.MYSTIC,
                20,
                VanillaEffectApply(PotionEffectType.POISON, linear(40 to 60, ctx), linear(1 to 2, ctx)),
                VanillaEffectApply(PotionEffectType.NAUSEA, linear(40 to 60, ctx), linear(1 to 2, ctx)),
            )
        )

        health(
            linear(60.0 to 120.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.3, ctx)
        )

        scale(1.7)

    }

}