package me.newburyminer.customItems.mobprovider.mobs.blackstone

import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.DeathSummonAbility
import me.newburyminer.customItems.mobprovider.ability.spell.SummonerAbility
import org.bukkit.entity.EntityType
import org.bukkit.entity.MagmaCube
import kotlin.math.roundToInt

object ReplicatingCube : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.BLACKSTONE
	override val tier: MobTier = MobTier.STANDARD
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.MAGMA_CUBE) {

        val size = (Math.random() * 0.85).roundToInt()

        ability(
            MeleeEffectAbility(
                damage(linear(18.0 to 36.0, ctx), CustomDamageType.MELEE),
                VanillaKnockbackApply(0.25)
            )
        )

        if (size == 1) {

            ability(
                DeathSummonAbility(
                    ReplicatingCube,
                    2
                )
            )

        }

        health(
            linear(25.0 to 50.0, ctx)
        )

        movementSpeed(
            linear(0.8 to 1.1, ctx)
        )

        scale(
            if (size == 0) 1.5
            else 2.5
        )

        apply {
            if (this !is MagmaCube) return@apply
            this.size = 0
        }

    }

}