package me.newburyminer.customItems.mobprovider.mobs.rocky

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
import me.newburyminer.customItems.mobprovider.ability.spell.LeapSlamAbility
import me.newburyminer.customItems.mobprovider.ability.spell.SummonerAbility
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType
import org.bukkit.potion.PotionEffectType

object SmashingSpider : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.ROCKY
	override val tier: MobTier = MobTier.STANDARD
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.SPIDER) {

        ability(
            MeleeEffectAbility(
                damage(linear(25.0 to 50.0, ctx), CustomDamageType.MELEE),
                VanillaEffectApply(PotionEffectType.MINING_FATIGUE, linear(20 to 40, ctx), 0),
                VanillaKnockbackApply()
            )
        )

        ability(
            LeapSlamAbility(
                linear(8.0 to 16.0, ctx),
                5.0,
                linear(2.5 to 3.5, ctx),
                linear(32.0 to 64.0, ctx),
                linear(0.4 to 1.4, ctx),
                linear(200 to 160, ctx),
                linear(20 to 10, ctx),
                ParticleTheme.ROCKY
            )
        )

        ability(
            SummonerAbility(
                linear(2 to 4, ctx),
                RockSpider,
                linear(40 to 30, ctx),
                linear(300 to 250, ctx),
                ParticleTheme.ROCKY
            )
        )

        health(
            linear(140.0 to 280.0, ctx)
        )

        movementSpeed(
            linear(1.2 to 1.5, ctx)
        )

        scale(0.8)

    }

}