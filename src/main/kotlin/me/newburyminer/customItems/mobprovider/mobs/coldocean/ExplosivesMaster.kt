package me.newburyminer.customItems.mobprovider.mobs.coldocean

import me.newburyminer.customItems.entity.hiteffects.effect.CustomKnockbackApply
import me.newburyminer.customItems.entity.hiteffects.effect.ProjectileKnockbackApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaEffectApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.ability.defensive.LeapDodgeAbility
import me.newburyminer.customItems.mobprovider.ability.projectile.ProjectileEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.ExplosiveGrenadeAbility
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector

object ExplosivesMaster: MobDefinition {
    
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.STRAY) {
    
        ability(
            ExplosiveGrenadeAbility(
                linear(10.0 to 15.0, ctx),
                1.5,
                Material.GRAY_CONCRETE_POWDER,
                linear(4.0 to 7.0, ctx),
                linear(2 to 4, ctx),
                linear(300 to 200, ctx)
            )
        )

        ability(
            ProjectileEffectAbility(
                damage(linear(30.0 to 60.0, ctx), CustomDamageType.PROJECTILE_NO_CD),
                VanillaEffectApply(PotionEffectType.SLOWNESS, linear(15 to 30, ctx), 6),
                CustomKnockbackApply(Vector(0, -2, 0))
            )
        )

        ability(
            LeapDodgeAbility(
                linear(0.25 to 0.5, ctx),
                linear(200 to 100, ctx),
                2.0
            )
        )
    
        health(
            linear(170.0 to 340.0, ctx)
        )
    
        movementSpeed(
            linear(1.1 to 1.5, ctx)
        )
    
    }
    
}