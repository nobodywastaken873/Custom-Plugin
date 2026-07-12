package me.newburyminer.customItems.mobprovider.mobs.blackstone

import me.newburyminer.customItems.entity.hiteffects.effect.DisableShieldApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern

object SwollenSkeleton : MobDefinition() {

    override val trim: ArmorTrim = ArmorTrim(TrimMaterial.DIAMOND, TrimPattern.HOST)
	override val tier: MobTier = MobTier.GRUNT
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.WITHER_SKELETON) {

        ability(
            MeleeEffectAbility(
                damage(linear(28.0 to 56.0, ctx), CustomDamageType.MELEE_NO_CD),
                DisableShieldApply(ignoreDirection = true),
                VanillaKnockbackApply(1.0)
            )
        )

        health(
            linear(36.0 to 72.0, ctx)
        )

        movementSpeed(
            linear(0.8 to 1.1, ctx)
        )

        scale(1.15)

        equipment {
            mainhand(Material.AIR)
        }
        
        equipment {
            mainhand(Material.NETHERITE_AXE)
            offhand(Material.AIR)
            setArmor(arrayOf(40, 32, 48), trim)
        }

    }

}