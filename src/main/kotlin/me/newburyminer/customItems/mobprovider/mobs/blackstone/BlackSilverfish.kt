package me.newburyminer.customItems.mobprovider.mobs.blackstone

import me.newburyminer.customItems.entity.components.melee.SuicideBomberComponent
import me.newburyminer.customItems.entity.components.spells.LeapComponent
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import org.bukkit.entity.EntityType

object BlackSilverfish : MobDefinition() {

    override val tier: MobTier = MobTier.GRUNT
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.ENDERMITE) {

        component(
            SuicideBomberComponent(
                HitEffects(
                    damage(linear(30.0 to 60.0, ctx), CustomDamageType.MELEE_NO_CD)
                )
            )
        )

        component(
            LeapComponent(
                linear(8.0 to 12.0, ctx),
                2.0,
                linear(150 to 100, ctx),
            )
        )

        health(
            linear(10.0 to 20.0, ctx)
        )

        movementSpeed(
            linear(0.8 to 1.2, ctx)
        )

    }

}