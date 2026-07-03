package me.newburyminer.customItems.mobprovider.mobs.warmocean

import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.DamageRadiusApply
import me.newburyminer.customItems.entity.hiteffects.effect.ProjectileKnockbackApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaEffectApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.ability.projectile.ProjectileEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.ArcingEffectAbility
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.potion.PotionEffectType

object SludgeTosser: MobDefinition {

    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.SKELETON) {

        ability(
            ArcingEffectAbility(
                linear(12.0 to 16.0, ctx),
                4.5,
                Material.SANDSTONE,
                linear(1 to 2, ctx),
                linear(100 to 80, ctx),
                DamageRadiusApply(
                    linear(2.0 to 2.5, ctx),
                    1.0,
                    HitEffects(damage(linear(26.0 to 52.0, ctx), CustomDamageType.MELEE_NO_CD)),
                    ParticleTheme.WARM_OCEAN
                )
            )
        )

        ability(
            ProjectileEffectAbility(
                damage(linear(24.0 to 48.0, ctx), CustomDamageType.PROJECTILE_NO_CD),
                VanillaEffectApply(PotionEffectType.WEAKNESS, linear(60 to 100, ctx), linear(0 to 2, ctx)),
                ProjectileKnockbackApply()
            )
        )

        health(
            linear(45.0 to 90.0, ctx)
        )

        movementSpeed(
            linear(0.8 to 1.0, ctx)
        )

    }

}