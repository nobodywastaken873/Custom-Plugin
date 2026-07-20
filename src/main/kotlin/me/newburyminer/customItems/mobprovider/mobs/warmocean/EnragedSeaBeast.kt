package me.newburyminer.customItems.mobprovider.mobs.warmocean

import me.newburyminer.customItems.entity.hiteffects.effect.CustomKnockbackApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.LeapSlamAbility
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType
import org.bukkit.util.Vector

object EnragedSeaBeast : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.WARM_OCEAN
	override val tier: MobTier = MobTier.STANDARD
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.ZOGLIN) {

        ability(
            MeleeEffectAbility(
                damage(linear(34.0 to 68.0, ctx), CustomDamageType.MELEE),
                attribute(
                    Attribute.MOVEMENT_SPEED,
                    -0.1,
                    AttributeModifier.Operation.ADD_SCALAR,
                    linear(40 to 80, ctx)
                ),
                CustomKnockbackApply(Vector(0.4, 0.8, 0.4))
            )
        )

        ability(
            LeapSlamAbility(
                linear(10.0 to 14.0, ctx),
                3.5,
                linear(2.5 to 3.5, ctx),
                linear(30.0 to 60.0, ctx),
                linear(0.4 to 1.2, ctx),
                linear(120 to 80, ctx),
                linear(30 to 20, ctx),
                ParticleTheme.WARM_OCEAN
            )
        )

        health(
            linear(70.0 to 140.0, ctx)
        )

        movementSpeed(
            linear(0.7 to 1.2, ctx)
        )

    }

}