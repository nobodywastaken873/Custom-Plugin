package me.newburyminer.customItems.mobprovider.mobs.military

import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.DamageRadiusApply
import me.newburyminer.customItems.entity.hiteffects.effect.EffectAuraApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.defensive.DamageShieldAbility
import me.newburyminer.customItems.mobprovider.ability.projectile.MachineGunAbility
import me.newburyminer.customItems.mobprovider.ability.spell.ArcingEffectAbility
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType

object MachineGunFortification : MobDefinition() {

	override val tier: MobTier = MobTier.ELITE
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.PILLAGER) {

        ability(
            MachineGunAbility(
                linear(5.0 to 9.0, ctx),
                linear(4 to 3, ctx),
                linear(10.0 to 14.0, ctx),
                -1.0
            )
        )

        ability(
            ArcingEffectAbility(
                linear(12.0 to 16.0, ctx),
                4.5,
                Material.LIGHT_GRAY_CONCRETE_POWDER,
                linear(1 to 2, ctx),
                linear(150 to 120, ctx),
                EffectAuraApply(
                    linear(2.0 to 2.5, ctx),
                    1.0,
                    linear(150 to 200, ctx),
                    HitEffects(
                        attribute(
                            Attribute.MOVEMENT_SPEED,
                            -0.5,
                            AttributeModifier.Operation.ADD_SCALAR,
                            linear(25 to 30, ctx)
                        )
                    ),
                    20,
                    ParticleTheme.MILITARY
                )
            )
        )

        ability(
            DamageShieldAbility(
                linear(2 to 4, ctx),
                linear(100 to 80, ctx),
                ParticleTheme.MILITARY
            )
        )

        health(
            linear(300.0 to 600.0, ctx)
        )

        movementSpeed(
            linear(0.8 to 1.0, ctx)
        )

        equipment {
            mainhand(Material.CROSSBOW)
        }

    }

}