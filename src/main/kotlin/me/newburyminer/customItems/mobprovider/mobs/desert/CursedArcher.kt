package me.newburyminer.customItems.mobprovider.mobs.desert

import me.newburyminer.customItems.entity.hiteffects.effect.ProjectileKnockbackApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaEffectApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.ability.projectile.ProjectileEffectAbility
import me.newburyminer.customItems.mobprovider.ability.projectile.ProjectileHomingAbility
import me.newburyminer.customItems.mobprovider.ability.spell.EffectMissileAbility
import org.bukkit.entity.EntityType
import org.bukkit.potion.PotionEffectType

object CursedArcher: MobDefinition {
    
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.PARCHED) {
    
        ability(
            ProjectileHomingAbility()
        )

        ability(
            ProjectileEffectAbility(
                damage(linear(20.0 to 40.0, ctx), CustomDamageType.PROJECTILE_NO_CD),
                VanillaEffectApply(PotionEffectType.WEAKNESS, linear(100 to 200, ctx), linear(0 to 1, ctx)),
                ProjectileKnockbackApply()
            )
        )

        ability(
            EffectMissileAbility(
                linear(20.0 to 30.0, ctx),
                0.05,
                1.5,
                linear(40 to 30, ctx),
                linear(300 to 200, ctx),
                ParticleTheme.DESERT,
                VanillaEffectApply(PotionEffectType.WITHER, linear(200 to 300, ctx), linear(1 to 2, ctx))
            )
        )
    
        health(
            linear(70.0 to 140.0, ctx)
        )
    
        movementSpeed(
            linear(1.0 to 1.4, ctx)
        )
    
    }
    
}