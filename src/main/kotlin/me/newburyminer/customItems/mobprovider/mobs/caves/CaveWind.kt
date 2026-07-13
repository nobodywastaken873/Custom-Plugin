package me.newburyminer.customItems.mobprovider.mobs.caves

import me.newburyminer.customItems.entity.components.utils.ProjectileType
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.projectile.MachineGunAbility
import me.newburyminer.customItems.mobprovider.ability.projectile.ProjectileHomingAbility
import org.bukkit.entity.EntityType

object CaveWind: MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.CAVES
    override val tier: MobTier = MobTier.ELITE
    override val targetRange: Double = 60.0
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.BREEZE) {

        ability(
            ProjectileHomingAbility()
        )

        ability(
            MachineGunAbility(
                linear(4.0 to 8.0, ctx),
                linear(10 to 8, ctx),
                linear(20.0 to 25.0, ctx),
                0.3,
                ProjectileType.WIND_CHARGE
            )
        )

        health(
            linear(240.0 to 480.0, ctx)
        )

        movementSpeed(
            linear(1.2 to 1.5, ctx)
        )

        apply {
            this.isInvisible = true
        }

    }

}