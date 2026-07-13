package me.newburyminer.customItems.mobprovider.mobs.caves

import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.ExplosiveGrenadeAbility
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern

object CaveGrenadier: MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.CAVES
    override val trim: TrimPattern = TrimPattern.BOLT
    override val tier: MobTier = MobTier.GRUNT
    override val targetRange: Double = 50.0
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.ZOMBIE) {

        ability(
            ExplosiveGrenadeAbility(
                linear(13.0 to 16.0, ctx),
                6.0,
                Material.GRAY_CONCRETE_POWDER,
                linear(4.0 to 7.0, ctx),
                linear(2 to 4, ctx),
                linear(300 to 200, ctx)
            )
        )

        ability(
            MeleeEffectAbility(
                damage(linear(25.0 to 50.0, ctx), CustomDamageType.MELEE_NO_CD),
                VanillaKnockbackApply()
            )
        )
    
        health(
            linear(65.0 to 130.0, ctx)
        )
    
        movementSpeed(
            linear(0.9 to 1.2, ctx)
        )
        
        equipment {
            mainhand(Material.DIAMOND_AXE)
            offhand(Material.AIR)
            setArmor(colorTheme.color, trim)
        }
    
    }
    
}