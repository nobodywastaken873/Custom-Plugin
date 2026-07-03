package me.newburyminer.customItems.mobprovider.mobs.blackstone

import me.newburyminer.customItems.entity.components.utils.ProjectileType
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaEffectApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.ability.projectile.MachineGunAbility
import me.newburyminer.customItems.mobprovider.ability.spell.EffectAuraAbility
import org.bukkit.entity.EntityType
import org.bukkit.potion.PotionEffectType

object EmblazenedSkeleton: MobDefinition {

    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.SKELETON) {

        ability(
            MachineGunAbility(
                linear(3.0 to 6.0, ctx),
                linear(4 to 3, ctx),
                linear(10.0 to 14.0, ctx),
                projectileType = ProjectileType.FIRE_CHARGE
            )
        )

        ability(
            EffectAuraAbility(
                linear(3.0 to 5.0, ctx),
                1.0,
                ParticleTheme.COLD_OCEAN,
                20,
                damage(linear(20.0 to 40.0, ctx), CustomDamageType.BURNING_NO_CD)
            )
        )

        health(
            linear(65.0 to 130.0, ctx)
        )

        movementSpeed(
            linear(0.7 to 0.9, ctx)
        )

    }

}