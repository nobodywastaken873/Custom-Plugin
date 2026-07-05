package me.newburyminer.customItems.mobprovider.mobs.caves

import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.SummonerAbility
import org.bukkit.entity.EntityType

object SwarmingSpider: MobDefinition() {

    override val tier: MobTier = MobTier.GRUNT
    override val targetRange: Double = 50.0
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.CAVE_SPIDER) {

        ability(
            MeleeEffectAbility(
                damage(linear(20.0 to 40.0, ctx), CustomDamageType.MELEE_NO_CD),
                VanillaKnockbackApply()
            )
        )

        ability(
            SummonerAbility(
                1,
                SwarmingSpider,
                linear(50 to 40, ctx),
                linear(300 to 250, ctx),
                ParticleTheme.CAVES
            )
        )

        health(
            linear(40.0 to 80.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.5, ctx)
        )

    }

}