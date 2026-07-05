package me.newburyminer.customItems.mobprovider.mobs.caves

import me.newburyminer.customItems.entity.components.melee.InvisibleCreakingComponent
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import org.bukkit.entity.EntityType

object CreepingCreaking: MobDefinition() {

    override val tier: MobTier = MobTier.STANDARD
    override val targetRange: Double = 50.0
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.CREAKING) {

        ability(
            MeleeEffectAbility(
                damage(linear(30.0 to 60.0, ctx), CustomDamageType.MELEE_NO_CD),
                VanillaKnockbackApply()
            )
        )

        component(
            InvisibleCreakingComponent()
        )

        health(
            linear(140.0 to 280.0, ctx)
        )

        movementSpeed(
            linear(0.7 * 4 to 0.8 * 4, ctx)
        )

    }

}