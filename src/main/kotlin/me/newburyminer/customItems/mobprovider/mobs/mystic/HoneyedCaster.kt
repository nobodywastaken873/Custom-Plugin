package me.newburyminer.customItems.mobprovider.mobs.mystic

import me.newburyminer.customItems.entity.components.projectileshooters.CustomWitchPotionShooter
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaEffectApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.projectile.ProjectileHomingAbility
import me.newburyminer.customItems.mobprovider.ability.spell.DamageAuraCastAbility
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object HoneyedCaster : MobDefinition() {

	override val tier: MobTier = MobTier.STANDARD
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.WITCH) {

        ability(
            DamageAuraCastAbility(
                linear(2.5 to 3.0, ctx),
                0.2,
                20,
                20,
                150,
                10,
                ParticleTheme.MYSTIC,
                linear(50 to 30, ctx),
                linear(250 to 150, ctx),
                linear(12.0 to 16.0, ctx),
                attribute(
                    Attribute.MOVEMENT_SPEED,
                    -0.1,
                    AttributeModifier.Operation.ADD_SCALAR,
                    linear(30 to 60, ctx)
                ),
                attribute(Attribute.JUMP_STRENGTH, -0.05, AttributeModifier.Operation.ADD_SCALAR, linear(30 to 60, ctx))
            )
        )

        component(
            CustomWitchPotionShooter(
                listOf(
                    PotionEffect(PotionEffectType.SLOWNESS, linear(50 to 100, ctx), linear(1 to 3, ctx)),
                    PotionEffect(PotionEffectType.WITHER, linear(50 to 100, ctx), linear(1 to 3, ctx))
                )
            )
        )

        ability(
            ProjectileHomingAbility()
        )

        health(
            linear(85.0 to 170.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.2, ctx)
        )

    }

}