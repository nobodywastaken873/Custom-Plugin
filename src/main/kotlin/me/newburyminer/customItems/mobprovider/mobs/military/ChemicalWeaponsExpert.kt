package me.newburyminer.customItems.mobprovider.mobs.military

import me.newburyminer.customItems.entity.components.projectileshooters.CustomWitchPotionShooter
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.EffectAuraApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaEffectApply
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.ability.projectile.ProjectileHomingAbility
import me.newburyminer.customItems.mobprovider.ability.spell.DamageAuraCastAbility
import me.newburyminer.customItems.mobprovider.ability.spell.EffectMissileAbility
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object ChemicalWeaponsExpert: MobDefinition {
    
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.WITCH) {

        ability(
            DamageAuraCastAbility(
                linear(2.5 to 3.0, ctx),
                1.0,
                40,
                20,
                150,
                10,
                ParticleTheme.MILITARY,
                linear(50 to 30, ctx),
                linear(250 to 150, ctx),
                linear(16.0 to 24.0, ctx),
                VanillaEffectApply(PotionEffectType.POISON, linear(30 to 60, ctx), linear(1 to 3, ctx))
            )
        )

        ability(
            EffectMissileAbility(
                linear(20.0 to 30.0, ctx),
                0.05,
                linear(1.5 to 2.5, ctx),
                linear(40 to 30, ctx),
                linear(250 to 220, ctx),
                ParticleTheme.MYSTIC,
                VanillaEffectApply(PotionEffectType.SLOWNESS, linear(100 to 150, ctx), linear(1 to 3, ctx)),
                VanillaEffectApply(PotionEffectType.MINING_FATIGUE, linear(100 to 150, ctx), linear(0 to 2, ctx))
            )
        )

        component(
            CustomWitchPotionShooter(
                listOf(
                    PotionEffect(PotionEffectType.WEAKNESS, linear(50 to 100, ctx), linear(1 to 3, ctx))
                )
            )
        )

        ability(
            ProjectileHomingAbility()
        )
    
        health(
            linear(240.0 to 480.0, ctx)
        )
    
        movementSpeed(
            linear(0.8 to 1.2, ctx)
        )
    
    }
    
}