package me.newburyminer.customItems.mobprovider.mobs.coldocean

import me.newburyminer.customItems.entity.components.melee.SuicideBomberComponent
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import org.bukkit.entity.EntityType

object CrazedPrisoner : MobDefinition() {

	override val tier: MobTier = MobTier.GRUNT
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.STRAY) {

        component(
            SuicideBomberComponent(
                HitEffects(
                    damage(linear(30.0 to 60.0, ctx), CustomDamageType.MELEE_NO_CD),
                    VanillaKnockbackApply()
                )
            )
        )

        health(
            linear(10.0 to 20.0, ctx)
        )

        movementSpeed(
            linear(1.5 to 1.75, ctx)
        )

    }

}