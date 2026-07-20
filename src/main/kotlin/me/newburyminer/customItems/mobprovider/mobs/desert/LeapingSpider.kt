package me.newburyminer.customItems.mobprovider.mobs.desert

import me.newburyminer.customItems.entity.hiteffects.effect.VanillaEffectApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobProvider
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.LeapSlamAbility
import org.bukkit.entity.EntityType
import org.bukkit.potion.PotionEffectType

object LeapingSpider : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.DESERT
	override val tier: MobTier = MobTier.STANDARD
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.CAVE_SPIDER) {

        ability(
            LeapSlamAbility(
                linear(8.0 to 12.0, ctx),
                3.0,
                linear(2.0 to 3.0, ctx),
                linear(30.0 to 60.0, ctx),
                0.8,
                linear(300 to 200, ctx),
                linear(20 to 10, ctx),
                ParticleTheme.DESERT
            )
        )

        ability(
            MeleeEffectAbility(
                damage(linear(20.0 to 45.0, ctx), CustomDamageType.MELEE),
                VanillaEffectApply(PotionEffectType.POISON, linear(40 to 80, ctx), linear(0 to 2, ctx)),
                VanillaKnockbackApply()
            )
        )

        health(
            linear(60.0 to 120.0, ctx)
        )

        movementSpeed(
            linear(1.25 to 1.5, ctx)
        )

    }

}