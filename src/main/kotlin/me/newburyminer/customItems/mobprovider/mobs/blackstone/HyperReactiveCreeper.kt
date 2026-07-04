package me.newburyminer.customItems.mobprovider.mobs.blackstone

import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.creeper.ChainExplosionAbility
import me.newburyminer.customItems.mobprovider.ability.creeper.CustomExplosionAbility
import me.newburyminer.customItems.mobprovider.ability.spell.EffectAuraAbility
import me.newburyminer.customItems.mobprovider.ability.spell.SummonerAbility
import org.bukkit.entity.Creeper
import org.bukkit.entity.EntityType

object HyperReactiveCreeper : MobDefinition() {

	override val tier: MobTier = MobTier.ELITE
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.CREEPER) {

        ability(
            CustomExplosionAbility(
                linear(4.0 to 8.0, ctx),
                true
            )
        )

        ability(
            ChainExplosionAbility()
        )

        ability(
            EffectAuraAbility(
                linear(2.5 to 4.5, ctx),
                1.0,
                ParticleTheme.BLACKSTONE,
                10,
                damage(linear(15.0 to 30.0, ctx), CustomDamageType.BURNING_NO_CD)
            )
        )

        ability(
            SummonerAbility(
                linear(3 to 6, ctx),
                InertCreeper,
                linear(40 to 30, ctx),
                linear(150 to 120, ctx),
                ParticleTheme.BLACKSTONE
            )
        )

        health(
            linear(300.0 to 600.0, ctx)
        )

        movementSpeed(
            linear(0.9 to 1.1, ctx)
        )

        scale(1.2)

        apply {
            if (this !is Creeper) return@apply
            this.maxFuseTicks = 200
        }

    }

}