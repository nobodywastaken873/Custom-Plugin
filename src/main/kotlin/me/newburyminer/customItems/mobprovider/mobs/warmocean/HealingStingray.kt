package me.newburyminer.customItems.mobprovider.mobs.warmocean

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
import me.newburyminer.customItems.mobprovider.ability.spell.HealerAbility
import org.bukkit.entity.EntityType

object HealingStingray : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.WARM_OCEAN
	override val tier: MobTier = MobTier.STANDARD
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.SILVERFISH) {

        ability(
            HealerAbility(
                linear(15.0 to 20.0, ctx),
                linear(15.0 to 25.0, ctx),
                linear(5.0 to 8.0, ctx),
                linear(50 to 40, ctx),
                linear(100 to 90, ctx),
            )
        )

        ability(
            DamageShieldAbility(
                linear(1 to 2, ctx),
                linear(80 to 60, ctx),
                ParticleTheme.WARM_OCEAN
            )
        )

        ability(
            MeleeEffectAbility(
                damage(linear(22.0 to 44.0, ctx), CustomDamageType.MELEE),
                VanillaKnockbackApply()
            )
        )

        health(
            linear(70.0 to 140.0, ctx)
        )

        movementSpeed(
            linear(0.6 to 0.8, ctx)
        )

        scale(2.0)

    }

}