package me.newburyminer.customItems.mobprovider.mobs.mystic

import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.BasicSlashAbility
import me.newburyminer.customItems.mobprovider.ability.spell.ExplosiveGrenadeAbility
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern

object HulkingPigman : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.MYSTIC
    override val trim: TrimPattern = TrimPattern.RIB
	override val tier: MobTier = MobTier.ELITE
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.ZOMBIFIED_PIGLIN) {

        ability(
            MeleeEffectAbility(
                damage(linear(29.0 to 58.0, ctx), CustomDamageType.MELEE),
                VanillaKnockbackApply()
            )
        )

        ability(
            ExplosiveGrenadeAbility(
                linear(10.0 to 15.0, ctx),
                1.5,
                Material.GRAY_CONCRETE_POWDER,
                linear(4.0 to 7.0, ctx),
                linear(2 to 4, ctx),
                linear(300 to 200, ctx)
            )
        )

        ability(
            BasicSlashAbility(
                linear(2 to 4, ctx),
                linear(18.0 to 36.0, ctx),
                linear(120 to 50, ctx),
                ParticleTheme.MYSTIC
            )
        )

        health(
            linear(320.0 to 640.0, ctx)
        )

        movementSpeed(
            linear(0.65 to 0.9, ctx)
        )

        scale(
            1.2
        )

        attribute(
            Attribute.KNOCKBACK_RESISTANCE, 1.0, AttributeModifier.Operation.ADD_NUMBER
        )
        
        equipment {
            mainhand(Material.WOODEN_AXE)
            offhand(Material.SHIELD)
            setArmor(colorTheme.color, trim)
        }

    }

}