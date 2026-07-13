package me.newburyminer.customItems.mobprovider.mobs.surface

import me.newburyminer.customItems.entity.components.projectileshooters.ElytraBreakerShooter
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.projectile.ElytraBreakerAbility
import me.newburyminer.customItems.mobprovider.ability.projectile.ProjectileEffectAbility
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern

object SAMInfantry: MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.SURFACE
    override val trim: TrimPattern = TrimPattern.EYE
    override val tier: MobTier = MobTier.STANDARD
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.PARCHED) {

        ability(
            ElytraBreakerAbility(
                linear(25.0 to 50.0, ctx),
                linear(200 to 150, ctx),
                linear(200 to 300, ctx),
                linear(1.0 to 2.0, ctx),
            )
        )

        ability(
            ProjectileEffectAbility(
                damage(linear(22.0 to 44.0, ctx), CustomDamageType.PROJECTILE_NO_CD)
            )
        )

        health(
            linear(75.0 to 150.0, ctx)
        )

        movementSpeed(
            linear(1.3 to 1.6, ctx)
        )

        equipment {
            mainhand(Material.BOW)
            offhand(Material.FIREWORK_ROCKET)
            setArmor(colorTheme.color, trim)
        }

    }

}