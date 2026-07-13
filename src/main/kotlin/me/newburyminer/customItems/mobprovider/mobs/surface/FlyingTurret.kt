package me.newburyminer.customItems.mobprovider.mobs.surface

import me.newburyminer.customItems.entity.components.projectileshooters.HomingProjectileShooter
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.projectile.ProjectileHomingAbility
import org.bukkit.entity.EntityType
import org.bukkit.entity.Ghast

object FlyingTurret: MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.SURFACE
    override val tier: MobTier = MobTier.STANDARD
    override val targetRange: Double = 100.0
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.GHAST) {
    
        ability(
            ProjectileHomingAbility()
        )
    
        health(
            linear(35.0 to 70.0, ctx)
        )
    
        movementSpeed(
            linear(0.7 * 4 to 0.9 * 4, ctx)
        )
        
        apply { 
            if (this !is Ghast) return@apply
            this.explosionPower = linear(3 to 7, ctx)
        }
    
    }
    
}