package me.newburyminer.customItems.mobprovider.mobs.warmocean

import me.newburyminer.customItems.entity.hiteffects.effect.ProjectileKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.ability.projectile.ProjectileEffectAbility
import me.newburyminer.customItems.mobprovider.ability.projectile.ProjectileHomingAbility
import me.newburyminer.customItems.mobprovider.ability.spell.EffectMissileAbility
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType

object UpperWindGod: MobDefinition {

    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.BREEZE) {

        ability(
            EffectMissileAbility(
                linear(20.0 to 30.0, ctx),
                0.05,
                linear(1.2 to 2.4, ctx),
                linear(40 to 30, ctx),
                linear(150 to 100, ctx),
                ParticleTheme.WARM_OCEAN,
                damage(linear(18.0 to 36.0, ctx), CustomDamageType.MAGIC_NO_CD),
                ProjectileKnockbackApply()
            )
        )

        ability(
            ProjectileHomingAbility()
        )

        ability(
            ProjectileEffectAbility(
                damage(linear(24.0 to 48.0, ctx), CustomDamageType.PROJECTILE_NO_CD)
            )
        )

        health(
            linear(160.0 to 320.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.2, ctx)
        )

    }

}