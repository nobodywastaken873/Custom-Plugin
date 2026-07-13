package me.newburyminer.customItems.mobprovider.mobs.coldocean

import me.newburyminer.customItems.entity.hiteffects.effect.DisableShieldApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.mobprovider.ColorTheme
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

object Buccaneer : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.COLD_OCEAN
    override val trim: TrimPattern = TrimPattern.WARD
	override val tier: MobTier = MobTier.GRUNT
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.DROWNED) {

        ability(
            MeleeEffectAbility(
                damage(linear(25.0 to 45.0, ctx), CustomDamageType.MELEE_NO_CD),
                DisableShieldApply(ignoreDirection = true),
                VanillaKnockbackApply()
            )
        )

        health(
            linear(40.0 to 80.0, ctx)
        )

        movementSpeed(
            linear(1.1 to 1.5, ctx)
        )

        equipment {
            mainhand(Material.IRON_AXE)
            offhand(Material.AIR)
            setArmor(colorTheme.color, trim)
        }

    }

}