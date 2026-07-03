package me.newburyminer.customItems.mobprovider.mobs.mystic

import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.DamageRadiusApply
import me.newburyminer.customItems.entity.hiteffects.effect.SummonMobsApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaEffectApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.ArcingEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.EffectMissileAbility
import me.newburyminer.customItems.mobprovider.ability.spell.SummonerAbility
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.potion.PotionEffectType

object QueenBee: MobDefinition {

    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.BEE) {

        ability(
            SummonerAbility(
                linear(4 to 8, ctx),
                UnstableHornet,
                linear(40 to 30, ctx),
                linear(300 to 250, ctx),
                ParticleTheme.MYSTIC
            )
        )

        ability(
            SummonerAbility(
                linear(2 to 4, ctx),
                HoneyedCaster,
                linear(40 to 30, ctx),
                linear(300 to 250, ctx),
                ParticleTheme.MYSTIC
            )
        )

        ability(
            MeleeEffectAbility(
                damage(linear(30.0 to 60.0, ctx), CustomDamageType.MELEE_NO_CD),
                VanillaEffectApply(PotionEffectType.POISON, linear(40 to 80, ctx), linear(2 to 4, ctx)),
                VanillaKnockbackApply()
            )
        )

        ability(
            ArcingEffectAbility(
                linear(12.0 to 16.0, ctx),
                2.0,
                Material.BEEHIVE,
                linear(1 to 2, ctx),
                linear(100 to 80, ctx),
                SummonMobsApply(
                    UnstableHornet,
                    linear(2 to 4, ctx),
                )
            )
        )

        ability(
            EffectMissileAbility(
                linear(20.0 to 30.0, ctx),
                0.05,
                linear(0.4 to 0.7, ctx),
                linear(40 to 30, ctx),
                linear(200 to 150, ctx),
                ParticleTheme.MYSTIC,
                SummonMobsApply(UnstableHornet, linear(2 to 4, ctx)),
            )
        )

        health(
            linear(1000.0 to 2000.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.2, ctx)
        )

        scale(3.0)

    }

}