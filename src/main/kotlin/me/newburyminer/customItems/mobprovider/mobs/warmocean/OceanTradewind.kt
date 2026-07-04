package me.newburyminer.customItems.mobprovider.mobs.warmocean

import me.newburyminer.customItems.entity.components.utils.ProjectileType
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.defensive.BasicDodgeAbility
import me.newburyminer.customItems.mobprovider.ability.projectile.MachineGunAbility
import org.bukkit.entity.EntityType

object OceanTradewind : MobDefinition() {

	override val tier: MobTier = MobTier.STANDARD
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.BREEZE) {

        ability(
            MachineGunAbility(
                linear(8.0 to 16.0, ctx),
                linear(10 to 8, ctx),
                linear(8.0 to 12.0, ctx),
                0.0,
                ProjectileType.WIND_CHARGE
            )
        )

        ability(
            BasicDodgeAbility(
                linear(0.21 to 0.4, ctx),
            )
        )

        health(
            linear(75.0 to 150.0, ctx)
        )

        movementSpeed(
            linear(1.3 to 1.6, ctx)
        )

    }

}