package me.newburyminer.customItems.mobprovider.mobs.caves

import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.projectile.MachineGunAbility
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern

object RapidFireSkeleton: MobDefinition() {

    override val trim: ArmorTrim = ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.FLOW)
    override val tier: MobTier = MobTier.STANDARD
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
            linear(50.0 to 100.0, ctx)
        )
    
        movementSpeed(
            linear(1.2 to 1.5, ctx)
        )

        equipment {
            mainhand(Material.BOW)
            offhand(Material.CROSSBOW)
            setArmor(arrayOf(111, 117, 117), trim)
        }
    
    }
    
}