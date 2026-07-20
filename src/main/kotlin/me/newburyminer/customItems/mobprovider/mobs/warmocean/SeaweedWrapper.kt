package me.newburyminer.customItems.mobprovider.mobs.warmocean

import me.newburyminer.customItems.entity.components.projectileshooters.HomingProjectileShooter
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.HomingSystem
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.projectile.ProjectileEffectAbility
import me.newburyminer.customItems.mobprovider.ability.projectile.ProjectileHomingAbility
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern

object SeaweedWrapper : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.WARM_OCEAN
    override val trim: TrimPattern = TrimPattern.COAST
	override val tier: MobTier = MobTier.GRUNT
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.SKELETON) {

        ability(
            ProjectileEffectAbility(
                damage(linear(20.0 to 40.0, ctx), CustomDamageType.PROJECTILE),
                attribute(
                    Attribute.MOVEMENT_SPEED,
                    linear(-0.5 to -0.8, ctx),
                    AttributeModifier.Operation.ADD_SCALAR,
                    linear(40 to 60, ctx)
                ),
                attribute(
                    Attribute.JUMP_STRENGTH,
                    linear(-0.5 to -0.8, ctx),
                    AttributeModifier.Operation.ADD_SCALAR,
                    linear(40 to 60, ctx)
                ),
            )
        )

        ability(
            ProjectileHomingAbility(
                homingType = HomingSystem.Type.ANGLE_SCALED
            )
        )

        health(
            linear(35.0 to 70.0, ctx)
        )

        movementSpeed(
            linear(1.2 to 1.5, ctx)
        )
        
        equipment {
            mainhand(Material.BOW)
            offhand(Material.AIR)
            setArmor(colorTheme.color, trim)
        }

    }

}