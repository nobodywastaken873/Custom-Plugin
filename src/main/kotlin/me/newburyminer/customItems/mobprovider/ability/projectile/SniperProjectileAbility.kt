package me.newburyminer.customItems.mobprovider.ability.projectile

import me.newburyminer.customItems.effects.AttributeData
import me.newburyminer.customItems.effects.CustomEffectType
import me.newburyminer.customItems.effects.EffectData
import me.newburyminer.customItems.entity.components.projectileshooters.CancelProjectiles
import me.newburyminer.customItems.entity.components.projectileshooters.MachineGunShooter
import me.newburyminer.customItems.entity.components.projectileshooters.SniperProjectileShooter
import me.newburyminer.customItems.entity.components.spells.SpellCasterComponent
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
import org.bukkit.damage.DamageType

class SniperProjectileAbility(
    val damage: Double,
    val cooldown: Int,
    val stunDuration: Int = 0,
    val damageType: DamageType = CustomDamageType.PROJECTILE,
    val projectileType: ProjectileType = ProjectileType.ARROW
): MobAbility {

    override fun MobBuilder.applyAbility(ctx: MobContext) {
        component(
            SniperProjectileShooter(
                cooldown,
                HitEffects(
                    CustomDamageApply(damage, damageType),
                    CustomEffectApply(
                        CustomEffectType.ATTRIBUTE,
                        EffectData(stunDuration,
                            AttributeData(-1.0, Attribute.MOVEMENT_SPEED, AttributeModifier.Operation.MULTIPLY_SCALAR_1)
                        )
                    ),
                    CustomEffectApply(
                        CustomEffectType.ATTRIBUTE,
                        EffectData(stunDuration,
                            AttributeData(-1.0, Attribute.JUMP_STRENGTH, AttributeModifier.Operation.MULTIPLY_SCALAR_1)
                        )
                    )
                ),
                projectileType
            )
        )

        component(
            CancelProjectiles()
        )

        component(
            SpellCasterComponent(-1.0)
        )

        equipment {
            mainhand(Material.BOW)
        }
    }

}