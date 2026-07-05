package me.newburyminer.customItems.mobprovider.mobs.caves

import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.projectile.MachineGunAbility
import org.bukkit.entity.EntityType

object RapidFireSkeleton: MobDefinition() {
    
    override val tier: MobTier = MobTier.GRUNT
    override val targetRange: Double = 50.0
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.SKELETON) {
    
        ability(
            MachineGunAbility(
                linear(8.0 to 16.0, ctx),
                linear(8 to 6, ctx),
                linear(12.0 to 16.0, ctx),
                -0.6,
            )
        )
    
        health(
            linear(35.0 to 70.0, ctx)
        )
    
        movementSpeed(
            linear(1.2 to 1.5, ctx)
        )
    
    }
    
}