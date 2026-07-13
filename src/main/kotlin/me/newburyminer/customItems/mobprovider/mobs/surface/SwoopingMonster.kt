package me.newburyminer.customItems.mobprovider.mobs.surface

import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.defensive.DamageShieldAbility
import org.bukkit.entity.EntityType

object SwoopingMonster: MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.SURFACE
    override val tier: MobTier = MobTier.STANDARD
    override val targetRange: Double = 50.0
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.PHANTOM) {

        ability(
            MeleeEffectAbility(
                damage(linear(24.0 to 48.0, ctx), CustomDamageType.MELEE_NO_CD),
                VanillaKnockbackApply()
            )
        )

        ability(
            DamageShieldAbility(
                1,
                linear(80 to 60, ctx),
                ParticleTheme.SURFACE
            )
        )

        health(
            linear(50.0 to 100.0, ctx)
        )

        movementSpeed(
            linear(0.7 * 4 to 0.8 * 4, ctx)
        )

        scale(1.4)

    }

}