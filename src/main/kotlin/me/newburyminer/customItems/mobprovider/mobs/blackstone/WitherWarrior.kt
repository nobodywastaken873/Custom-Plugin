package me.newburyminer.customItems.mobprovider.mobs.blackstone

import me.newburyminer.customItems.entity.hiteffects.effect.VanillaEffectApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import org.bukkit.entity.EntityType
import org.bukkit.potion.PotionEffectType

object WitherWarrior: MobDefinition {

    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.WITHER_SKELETON) {

        ability(
            MeleeEffectAbility(
                damage(linear(27.0 to 54.0, ctx), CustomDamageType.MELEE_NO_CD),
                VanillaEffectApply(PotionEffectType.WITHER, linear(60 to 100, ctx), linear(0 to 1, ctx)),
                VanillaKnockbackApply()
            )
        )

        health(
            linear(45.0 to 90.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.5, ctx)
        )

        scale(0.82)

    }

}