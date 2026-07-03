package me.newburyminer.customItems.mobprovider.mobs.desert

import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.ability.defensive.LeapDodgeAbility
import me.newburyminer.customItems.mobprovider.ability.projectile.SniperProjectileAbility
import me.newburyminer.customItems.mobprovider.ability.spell.EffectMissileAbility
import org.bukkit.entity.EntityType

object SandySniper: MobDefinition {
    
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.PARCHED) {
    
        ability(
            SniperProjectileAbility(
                linear(40.0 to 80.0, ctx),
                linear(400 to 300, ctx),
                linear(0 to 20, ctx)
            )
        )

        ability(
            LeapDodgeAbility(
                linear(0.3 to 0.6, ctx),
                linear(100 to 50, ctx),
                linear(0.6 to 1.2, ctx)
            )
        )

        ability(
            EffectMissileAbility(
                linear(30.0 to 40.0, ctx),
                0.05,
                linear(2.0 to 3.0, ctx),
                linear(30 to 20, ctx),
                linear(70 to 40, ctx),
                ParticleTheme.DESERT,
                damage(linear(30.0 to 60.0, ctx), CustomDamageType.PROJECTILE_NO_CD),
                VanillaKnockbackApply(0.1)
            )
        )
    
        health(
            linear(60.0 to 120.0, ctx)
        )
    
        movementSpeed(
            linear(0.5 to 0.6, ctx)
        )
    
    }
    
}