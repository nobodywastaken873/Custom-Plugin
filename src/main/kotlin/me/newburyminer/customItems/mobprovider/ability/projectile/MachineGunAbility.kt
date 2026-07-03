package me.newburyminer.customItems.mobprovider.ability.projectile

import me.newburyminer.customItems.entity.components.projectileshooters.CancelProjectiles
import me.newburyminer.customItems.entity.components.projectileshooters.MachineGunShooter
import me.newburyminer.customItems.entity.components.utils.ProjectileType
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.CustomDamageApply
import me.newburyminer.customItems.entity.hiteffects.effect.ProjectileKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.mobprovider.MobAbility
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import org.bukkit.Material

class MachineGunAbility(
    val damage: Double,
    val delay: Int,
    val range: Double,
    val slowdown: Double = 0.5,
    val projectileType: ProjectileType = ProjectileType.ARROW
): MobAbility {

    override fun MobBuilder.apply(ctx: MobContext) {
        component(
            MachineGunShooter(
                HitEffects(
                    CustomDamageApply(damage, CustomDamageType.PROJECTILE_NO_CD),
                    ProjectileKnockbackApply(0.05)
                ),
                delay,
                range,
                slowdown,
                projectileType
            )
        )

        component(
            CancelProjectiles()
        )

        equipment {
            mainhand(Material.BOW)
        }
    }

}