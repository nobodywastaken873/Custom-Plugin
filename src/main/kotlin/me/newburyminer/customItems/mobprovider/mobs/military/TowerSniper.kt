package me.newburyminer.customItems.mobprovider.mobs.military

import me.newburyminer.customItems.entity.components.utils.ProjectileType
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.projectile.SniperProjectileAbility
import me.newburyminer.customItems.mobprovider.ability.spell.BeamShooterAbility
import org.bukkit.Material
import org.bukkit.entity.EntityType

object TowerSniper : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.MILITARY
	override val tier: MobTier = MobTier.STANDARD
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.PILLAGER) {

        ability(
            SniperProjectileAbility(
                linear(35.0 to 75.0, ctx),
                linear(250 to 220, ctx),
                linear(10 to 30, ctx),
                projectileType = ProjectileType.ARROW
            )
        )

        ability(
            BeamShooterAbility(
                linear(25.0 to 40.0, ctx),
                false,
                linear(30 to 15, ctx),
                linear(150 to 120, ctx),
                ParticleTheme.MILITARY,
                damage(linear(32.0 to 64.0, ctx), CustomDamageType.PROJECTILE_NO_CD)
            )
        )

        health(
            linear(80.0 to 160.0, ctx)
        )

        movementSpeed(
            linear(0.5 to 0.6, ctx)
        )

        equipment {
            mainhand(Material.CROSSBOW)
        }

    }

}