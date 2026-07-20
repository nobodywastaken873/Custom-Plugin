package me.newburyminer.customItems.mobprovider.mobs.blackstone

import me.newburyminer.customItems.entity.components.spells.LeapComponent
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.defensive.LeapDodgeAbility
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern

object DarkDuelist : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.BLACKSTONE
    override val trim: TrimPattern = TrimPattern.TIDE
	override val tier: MobTier = MobTier.STANDARD
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.WITHER_SKELETON) {

        ability(
            MeleeEffectAbility(
                damage(linear(26.0 to 52.0, ctx), CustomDamageType.MELEE),
                VanillaKnockbackApply()
            )
        )

        ability(
            LeapDodgeAbility(
                linear(0.2 to 0.4, ctx),
                linear(200 to 150, ctx),
                1.5
            )
        )

        component(
            LeapComponent(
                linear(8.0 to 12.0, ctx),
                0.8,
                linear(140 to 80, ctx),
            )
        )

        health(
            linear(140.0 to 280.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.5, ctx)
        )

        scale(0.82)

        equipment {
            mainhand(Material.NETHERITE_SWORD)
            offhand(Material.NETHERITE_SWORD)
            setArmor(colorTheme.color, trim)
        }

    }

}