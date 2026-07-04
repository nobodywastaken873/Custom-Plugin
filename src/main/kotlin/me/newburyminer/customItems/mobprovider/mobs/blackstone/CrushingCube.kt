package me.newburyminer.customItems.mobprovider.mobs.blackstone

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
import org.bukkit.entity.MagmaCube

object CrushingCube : MobDefinition() {

	override val tier: MobTier = MobTier.STANDARD
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.MAGMA_CUBE) {

        ability(
            LeapSlamAbility(
                linear(10.0 to 15.0, ctx),
                6.0,
                linear(3.0 to 5.0, ctx),
                linear(30.0 to 60.0, ctx),
                linear(0.4 to 0.8, ctx),
                linear(80 to 60, ctx),
                linear(20 to 10, ctx),
                ParticleTheme.BLACKSTONE
            )
        )

        ability(
            MeleeEffectAbility(
                damage(linear(25.0 to 50.0, ctx), CustomDamageType.MELEE_NO_CD),
                VanillaKnockbackApply()
            )
        )

        health(
            linear(85.0 to 170.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.5, ctx)
        )

        apply {
            if (this !is MagmaCube) return@apply
            this.size = 4
        }

    }

}