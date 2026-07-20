package me.newburyminer.customItems.mobprovider.mobs.caves

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
import me.newburyminer.customItems.mobprovider.ability.spell.SummonerAbility
import org.bukkit.entity.EntityType
import org.bukkit.potion.PotionEffectType

object CaveFairy: MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.CAVES
    override val tier: MobTier = MobTier.ELITE
    override val targetRange: Double = 50.0
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.VEX) {

        ability(
            MeleeEffectAbility(
                damage(linear(27.0 to 54.0, ctx), CustomDamageType.MELEE),
                VanillaEffectApply(PotionEffectType.MINING_FATIGUE, linear(150 to 200, ctx), linear(0 to 1, ctx)),
                VanillaKnockbackApply()
            )
        )

        ability(
            SummonerAbility(
                linear(2 to 4, ctx),
                RapidFireSkeleton,
                linear(40 to 30, ctx),
                linear(200 to 180, ctx),
                ParticleTheme.CAVES
            )
        )

        health(
            linear(150.0 to 300.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.2, ctx)
        )

        scale(0.7)

    }

}