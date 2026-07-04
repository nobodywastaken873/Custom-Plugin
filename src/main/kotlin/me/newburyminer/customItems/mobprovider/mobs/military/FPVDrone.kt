package me.newburyminer.customItems.mobprovider.mobs.military

import me.newburyminer.customItems.entity.components.melee.SuicideBomberComponent
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.ExplosionApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaEffectApply
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import org.bukkit.entity.EntityType
import org.bukkit.potion.PotionEffectType

object FPVDrone : MobDefinition() {

	override val tier: MobTier = MobTier.GRUNT
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.VEX) {

        component(
            SuicideBomberComponent(
                HitEffects(
                    ExplosionApply(linear(2.5 to 5.0, ctx).toFloat(), true),
                    VanillaEffectApply(PotionEffectType.GLOWING, linear(200 to 250, ctx), 0)
                )
            )
        )

        health(
            linear(10.0 to 20.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.2, ctx)
        )

    }

}