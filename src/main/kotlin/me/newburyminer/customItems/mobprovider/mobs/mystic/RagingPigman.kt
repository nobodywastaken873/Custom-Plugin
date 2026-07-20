package me.newburyminer.customItems.mobprovider.mobs.mystic

import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern

object RagingPigman : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.MYSTIC
    override val trim: TrimPattern = TrimPattern.SNOUT
	override val tier: MobTier = MobTier.GRUNT
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.ZOMBIFIED_PIGLIN) {

        ability(
            MeleeEffectAbility(
                damage(linear(24.0 to 48.0, ctx), CustomDamageType.MELEE),
                VanillaKnockbackApply()
            )
        )

        health(
            linear(30.0 to 60.0, ctx)
        )

        movementSpeed(
            linear(1.3 to 1.6, ctx)
        )

        attribute(
            Attribute.KNOCKBACK_RESISTANCE, 1.0, AttributeModifier.Operation.ADD_NUMBER
        )
        
        equipment {
            mainhand(Material.WOODEN_SPEAR)
            offhand(Material.AIR)
            setArmor(colorTheme.color, trim)
        }

    }

}