package me.newburyminer.customItems.mobprovider.mobs.caves

import me.newburyminer.customItems.entity.components.projectileshooters.CustomWitchPotionShooter
import me.newburyminer.customItems.entity.components.projectileshooters.HomingProjectileShooter
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaEffectApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.projectile.ProjectileHomingAbility
import me.newburyminer.customItems.mobprovider.ability.spell.DamageAuraCastAbility
import org.bukkit.entity.EntityType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object BewitchingDweller: MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.CAVES
    override val tier: MobTier = MobTier.STANDARD
    override val targetRange: Double = 50.0
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.WITCH) {

        ability(
            DamageAuraCastAbility(
                linear(3.5 to 5.0, ctx),
                1.0,
                40,
                20,
                400,
                10,
                ParticleTheme.CAVES,
                linear(50 to 30, ctx),
                linear(250 to 150, ctx),
                linear(20.0 to 28.0, ctx),
                damage(linear(12.0 to 24.0, ctx), CustomDamageType.MAGIC_NO_CD)
            )
        )

        ability(
            ProjectileHomingAbility()
        )

        component(
            CustomWitchPotionShooter(
                listOf(
                    PotionEffect(PotionEffectType.MINING_FATIGUE, linear(200 to 300, ctx), linear(0 to 2, ctx))
                )
            )
        )

        health(
            linear(70.0 to 140.0, ctx)
        )

        movementSpeed(
            linear(1.3 to 1.6, ctx)
        )

    }

}