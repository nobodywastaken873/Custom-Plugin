package me.newburyminer.customItems.mobprovider.mobs.desert

import me.newburyminer.customItems.entity.hiteffects.effect.CustomKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.HealerAbility
import org.bukkit.entity.EntityType
import org.bukkit.util.Vector

object AncientBeast : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.DESERT
	override val tier: MobTier = MobTier.ELITE
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.ZOGLIN) {

        ability(
            MeleeEffectAbility(
                damage(linear(30.0 to 55.0, ctx), CustomDamageType.MELEE),
                CustomKnockbackApply(Vector(0.4, 2.0, 0.4))
            )
        )

        ability(
            HealerAbility(
                linear(20.0 to 30.0, ctx),
                linear(10.0 to 20.0, ctx),
                linear(15.0 to 30.0, ctx),
                linear(40 to 20, ctx),
                linear(300 to 250, ctx),
            )
        )

        health(
            linear(300.0 to 600.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.1, ctx)
        )

        apply {
            val rider = BeastRider.build(ctx).createEntity(ctx)
            this.addPassenger(rider)
        }

    }

}