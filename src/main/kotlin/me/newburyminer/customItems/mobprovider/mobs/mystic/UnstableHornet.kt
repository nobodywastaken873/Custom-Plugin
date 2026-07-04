package me.newburyminer.customItems.mobprovider.mobs.mystic

import me.newburyminer.customItems.entity.components.melee.SuicideBomberComponent
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.ExplosionApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import org.bukkit.entity.EntityType

object UnstableHornet : MobDefinition() {

	override val tier: MobTier = MobTier.GRUNT
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.BEE) {

        component(
            SuicideBomberComponent(
                HitEffects(
                    damage(linear(20.0 to 40.0, ctx), CustomDamageType.MELEE_NO_CD)
                )
            )
        )

        health(
            linear(20.0 to 40.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.3, ctx)
        )

    }

}