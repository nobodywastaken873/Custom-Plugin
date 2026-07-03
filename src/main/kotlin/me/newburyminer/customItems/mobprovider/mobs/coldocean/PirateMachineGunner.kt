package me.newburyminer.customItems.mobprovider.mobs.coldocean

import me.newburyminer.customItems.entity.components.utils.ProjectileType
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.ability.defensive.BasicDodgeAbility
import me.newburyminer.customItems.mobprovider.ability.projectile.MachineGunAbility
import org.bukkit.entity.EntityType

object PirateMachineGunner: MobDefinition {

    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.STRAY) {

        ability(
            MachineGunAbility(
                linear(4.0 to 8.0, ctx),
                linear(5 to 4, ctx),
                linear(12.0 to 16.0, ctx),
                projectileType = ProjectileType.ARROW
            )
        )

        ability(
            BasicDodgeAbility(
                linear(0.2 to 0.3, ctx)
            )
        )

        health(
            linear(40.0 to 90.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.5, ctx)
        )

    }

}