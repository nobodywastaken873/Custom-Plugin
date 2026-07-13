package me.newburyminer.customItems.mobprovider.mobs.desert

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

object ScarabSoldier : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.DESERT
    override val trim: TrimPattern = TrimPattern.TIDE
	override val tier: MobTier = MobTier.GRUNT
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.PARCHED) {

        ability(
            MeleeEffectAbility(
                damage(linear(23.0 to 46.0, ctx), CustomDamageType.MELEE_NO_CD),
                attribute(Attribute.ARMOR, -1.0, AttributeModifier.Operation.ADD_NUMBER, linear(30 to 60, ctx)),
                VanillaKnockbackApply()
            )
        )

        health(
            linear(35.0 to 60.0, ctx)
        )

        movementSpeed(
            linear(1.25 to 1.6, ctx)
        )

        attribute(
            Attribute.KNOCKBACK_RESISTANCE, 1.0, AttributeModifier.Operation.ADD_NUMBER
        )

        equipment {
            mainhand(Material.GOLDEN_SWORD)
            offhand(Material.WIND_CHARGE)
            setArmor(colorTheme.color, trim)
        }

    }

}