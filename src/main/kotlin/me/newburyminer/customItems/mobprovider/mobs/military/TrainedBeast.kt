package me.newburyminer.customItems.mobprovider.mobs.military

import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.LeapSlamAbility
import org.bukkit.entity.EntityType

object TrainedBeast : MobDefinition() {

	override val tier: MobTier = MobTier.STANDARD
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.RAVAGER) {

        ability(
            MeleeEffectAbility(
                damage(linear(25.0 to 50.0, ctx), CustomDamageType.MELEE_NO_CD),
                VanillaKnockbackApply(0.5)
            )
        )

        ability(
            LeapSlamAbility(
                linear(8.0 to 16.0, ctx),
                3.0,
                linear(2.5 to 3.5, ctx),
                linear(32.0 to 64.0, ctx),
                linear(0.4 to 1.4, ctx),
                linear(200 to 160, ctx),
                linear(20 to 10, ctx),
                ParticleTheme.MILITARY
            )
        )

        health(
            linear(200.0 to 400.0, ctx)
        )

        movementSpeed(
            linear(1.1 to 1.3, ctx)
        )

    }

}