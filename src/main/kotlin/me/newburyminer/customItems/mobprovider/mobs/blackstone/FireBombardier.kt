package me.newburyminer.customItems.mobprovider.mobs.blackstone

import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.EffectAuraApply
import me.newburyminer.customItems.entity.hiteffects.effect.ProjectileKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.ability.projectile.ProjectileEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.ArcingEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.BasicSlashAbility
import org.bukkit.Material
import org.bukkit.entity.EntityType

object FireBombardier: MobDefinition {

    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.BLAZE) {

        ability(
            ArcingEffectAbility(
                linear(15.0 to 25.0, ctx),
                6.0,
                Material.MAGMA_BLOCK,
                linear(2 to 4, ctx),
                linear(400 to 300, ctx),
                EffectAuraApply(
                    linear(3.0 to 4.5, ctx),
                    1.0,
                    linear(120 to 200, ctx),
                    HitEffects(damage(linear(20.0 to 40.0, ctx), CustomDamageType.BURNING_NO_CD)),
                    10,
                    ParticleTheme.BLACKSTONE,
                )
            )
        )

        ability(
            ProjectileEffectAbility(
                damage(linear(25.0 to 50.0, ctx), CustomDamageType.BURNING_NO_CD),
                ProjectileKnockbackApply(0.3)
            )
        )

        ability(
            BasicSlashAbility(
                linear(2 to 4, ctx),
                linear(25.0 to 45.0, ctx),
                linear(150 to 100, ctx),
                ParticleTheme.BLACKSTONE
            )
        )

        health(
            linear(140.0 to 280.0, ctx)
        )

        movementSpeed(
            linear(0.8 to 1.2, ctx)
        )

    }

}