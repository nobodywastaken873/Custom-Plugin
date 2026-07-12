package me.newburyminer.customItems.mobprovider.mobs.surface

import me.newburyminer.customItems.entity.components.spells.LeapComponent
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

object ParchedZombie: MobDefinition() {

    override val trim: ArmorTrim = ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.WARD)
    override val tier: MobTier = MobTier.GRUNT
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.HUSK) {

        ability(
            MeleeEffectAbility(
                damage(linear(20.0 to 40.0, ctx), CustomDamageType.MELEE_NO_CD),
                attribute(Attribute.MOVEMENT_SPEED, -0.1, AttributeModifier.Operation.ADD_SCALAR, linear(150 to 200, ctx)),
                VanillaKnockbackApply()
            )
        )

        component(
            LeapComponent(
                linear(12.0 to 20.0, ctx),
                linear(6.0 to 7.0, ctx),
                linear(200 to 150, ctx),
            )
        )

        health(
            linear(35.0 to 70.0, ctx)
        )

        movementSpeed(
            linear(1.6 to 2.2, ctx)
        )

        equipment {
            mainhand(Material.STONE_SWORD)
            offhand(Material.AIR)
            setArmor(arrayOf(125, 122, 109), trim)
        }

    }

}