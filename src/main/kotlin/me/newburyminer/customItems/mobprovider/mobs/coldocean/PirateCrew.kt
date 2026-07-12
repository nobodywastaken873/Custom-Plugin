package me.newburyminer.customItems.mobprovider.mobs.coldocean

import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
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

object PirateCrew : MobDefinition() {

    override val trim: ArmorTrim = ArmorTrim(TrimMaterial.COPPER, TrimPattern.WARD)
	override val tier: MobTier = MobTier.GRUNT
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.DROWNED) {

        ability(
            MeleeEffectAbility(
                damage(
                    linear(20.0 to 40.0, ctx), CustomDamageType.MELEE_NO_CD
                ),
                VanillaKnockbackApply()
            )
        )

        health(
            linear(35.0 to 70.0, ctx)
        )

        movementSpeed(
            linear(1.2 to 1.7, ctx)
        )

        scale(0.9)

        equipment {
            mainhand(Material.IRON_SWORD)
            offhand(Material.AIR)
            setArmor(arrayOf(47, 64, 158), trim)
        }

    }

}