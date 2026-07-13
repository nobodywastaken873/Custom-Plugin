package me.newburyminer.customItems.mobprovider.mobs.desert

import me.newburyminer.customItems.entity.components.utils.ProjectileType
import me.newburyminer.customItems.entity.hiteffects.effect.CustomKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.projectile.MachineGunAbility
import me.newburyminer.customItems.mobprovider.ability.spell.EffectAuraAbility
import org.bukkit.entity.EntityType
import org.bukkit.util.Vector

object SandstormMage : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.DESERT
	override val tier: MobTier = MobTier.ELITE
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.BREEZE) {

        ability(
            MachineGunAbility(
                linear(5.0 to 10.0, ctx),
                linear(8 to 6, ctx),
                linear(8.0 to 12.0, ctx),
                linear(0.7 to 0.5, ctx),
                ProjectileType.WIND_CHARGE
            )
        )

        ability(
            EffectAuraAbility(
                linear(10.0 to 12.0, ctx),
                2.0,
                ParticleTheme.DESERT,
                10,
                CustomKnockbackApply(Vector(-2.0, 1.5, -2.0))
            )
        )

        ability(
            EffectAuraAbility(
                linear(3.0 to 4.0, ctx),
                2.0,
                ParticleTheme.DESERT,
                10,
                damage(linear(15.0 to 35.0, ctx), CustomDamageType.MELEE_NO_CD)
            )
        )

        health(
            linear(120.0 to 240.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.5, ctx)
        )

    }

}