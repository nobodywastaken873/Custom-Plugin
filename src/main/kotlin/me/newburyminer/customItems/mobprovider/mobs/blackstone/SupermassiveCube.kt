package me.newburyminer.customItems.mobprovider.mobs.blackstone

import me.newburyminer.customItems.entity.hiteffects.effect.CustomKnockbackApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.EffectAuraAbility
import me.newburyminer.customItems.mobprovider.ability.spell.TrackingBeamAbility
import org.bukkit.entity.EntityType
import org.bukkit.entity.MagmaCube
import org.bukkit.util.Vector

object SupermassiveCube : MobDefinition() {

	override val tier: MobTier = MobTier.ELITE
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.MAGMA_CUBE) {

        ability(
            MeleeEffectAbility(
                damage(linear(23.0 to 46.0, ctx), CustomDamageType.MELEE_NO_CD),
                VanillaKnockbackApply()
            )
        )

        ability(
            EffectAuraAbility(
                linear(4.0 to 8.0, ctx),
                2.0,
                ParticleTheme.BLACKSTONE,
                2,
                CustomKnockbackApply(Vector(-0.1, 0.1, -0.1))
            )
        )

        ability(
            TrackingBeamAbility(
                linear(20.0 to 30.0, ctx),
                linear(6 to 4, ctx),
                linear(400 to 600, ctx),
                linear(10 to 5, ctx),
                ParticleTheme.BLACKSTONE,
                damage(linear(10.0 to 15.0, ctx), CustomDamageType.BURNING_NO_CD)
            )
        )

        health(
            linear(360.0 to 720.0, ctx)
        )

        movementSpeed(
            linear(0.7 to 1.0, ctx)
        )

        apply {
            if (this !is MagmaCube) return@apply
            this.size = 0
        }

        scale(7.5)

    }

}