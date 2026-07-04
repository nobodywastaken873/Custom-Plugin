package me.newburyminer.customItems.mobprovider.ability.projectile

import me.newburyminer.customItems.effects.AttributeData
import me.newburyminer.customItems.effects.CustomEffectType
import me.newburyminer.customItems.effects.EffectData
import me.newburyminer.customItems.entity.components.projectileshooters.CancelProjectiles
import me.newburyminer.customItems.entity.components.projectileshooters.ElytraBreakerShooter
import me.newburyminer.customItems.entity.components.projectileshooters.MachineGunShooter
import me.newburyminer.customItems.entity.components.utils.ProjectileType
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.CustomDamageApply
import me.newburyminer.customItems.entity.hiteffects.effect.CustomEffectApply
import me.newburyminer.customItems.entity.hiteffects.effect.ProjectileKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.mobprovider.MobAbility
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier

class ElytraBreakerAbility(
    val damage: Double,
    val cooldown: Int,
    val disableDuration: Int,
    val stunMultiplier: Double = 0.0,
): MobAbility {

    override fun MobBuilder.applyAbility(ctx: MobContext) {
        component(
            ElytraBreakerShooter(
                HitEffects(
                    CustomDamageApply(damage, CustomDamageType.EXPLOSION),
                    CustomEffectApply(
                        CustomEffectType.ATTRIBUTE,
                        EffectData(100,
                            AttributeData(stunMultiplier, Attribute.FALL_DAMAGE_MULTIPLIER, AttributeModifier.Operation.ADD_NUMBER)
                        )
                    ),
                    CustomEffectApply(
                        CustomEffectType.ATTRIBUTE,
                        EffectData(100,
                            AttributeData(stunMultiplier, Attribute.GRAVITY, AttributeModifier.Operation.ADD_SCALAR)
                        )
                    )
                ),
                cooldown,
                disableDuration
            )
        )
    }

}