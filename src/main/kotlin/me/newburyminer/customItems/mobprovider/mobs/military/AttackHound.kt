package me.newburyminer.customItems.mobprovider.mobs.military

import me.newburyminer.customItems.entity.components.spells.LeapComponent
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaEffectApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import org.bukkit.entity.EntityType
import org.bukkit.potion.PotionEffectType

object AttackHound: MobDefinition {

    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.WOLF) {

        ability(
            MeleeEffectAbility(
                damage(linear(22.0 to 44.4, ctx), CustomDamageType.MELEE_NO_CD),
                VanillaEffectApply(PotionEffectType.WITHER, linear(40 to 60, ctx), linear(0 to 1, ctx)),
                VanillaKnockbackApply()
            )
        )

        component(
            LeapComponent(
                linear(12.0 to 14.0, ctx),
                0.8,
                linear(200 to 180, ctx),
            )
        )

        health(
            linear(30.0 to 60.0, ctx)
        )

        movementSpeed(
            linear(1.4 to 1.8, ctx)
        )

        scale(1.2)

    }

}