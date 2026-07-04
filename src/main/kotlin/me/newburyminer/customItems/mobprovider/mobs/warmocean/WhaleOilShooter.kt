package me.newburyminer.customItems.mobprovider.mobs.warmocean

import me.newburyminer.customItems.entity.hiteffects.effect.ExplosionApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaEffectApply
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.projectile.ProjectileEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.EffectAuraAbility
import me.newburyminer.customItems.mobprovider.ability.spell.EffectMissileAbility
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType
import org.bukkit.potion.PotionEffectType

object WhaleOilShooter : MobDefinition() {

	override val tier: MobTier = MobTier.ELITE
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.SKELETON) {

        ability(
            ProjectileEffectAbility(
                ExplosionApply(
                    linear(2.5 to 5.0, ctx).toFloat(),
                    true
                )
            )
        )

        ability(
            EffectAuraAbility(
                linear(3.5 to 4.5, ctx),
                1.0,
                ParticleTheme.WARM_OCEAN,
                20,
                VanillaEffectApply(PotionEffectType.LEVITATION, linear(40 to 60, ctx), linear(3 to 6, ctx))
            )
        )

        ability(
            EffectMissileAbility(
                linear(20.0 to 30.0, ctx),
                0.05,
                1.5,
                linear(40 to 30, ctx),
                linear(150 to 100, ctx),
                ParticleTheme.WARM_OCEAN,
                attribute(
                    Attribute.FALL_DAMAGE_MULTIPLIER,
                    linear(3.0 to 6.0, ctx),
                    AttributeModifier.Operation.ADD_NUMBER,
                    linear(150 to 200, ctx)
                )
            )
        )

        health(
            linear(200.0 to 400.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.3, ctx)
        )

        scale(
            1.2
        )

    }

}