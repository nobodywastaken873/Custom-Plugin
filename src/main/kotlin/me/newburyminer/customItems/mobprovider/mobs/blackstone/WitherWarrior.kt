package me.newburyminer.customItems.mobprovider.mobs.blackstone

import me.newburyminer.customItems.entity.hiteffects.effect.VanillaEffectApply
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
import org.bukkit.potion.PotionEffectType

object WitherWarrior : MobDefinition() {

    override val trim: ArmorTrim = ArmorTrim(TrimMaterial.DIAMOND, TrimPattern.WARD)
	override val tier: MobTier = MobTier.STANDARD
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.WITHER_SKELETON) {

        ability(
            MeleeEffectAbility(
                damage(linear(27.0 to 54.0, ctx), CustomDamageType.MELEE_NO_CD),
                VanillaEffectApply(PotionEffectType.WITHER, linear(60 to 100, ctx), linear(1 to 2, ctx)),
                VanillaKnockbackApply()
            )
        )

        health(
            linear(55.0 to 120.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.5, ctx)
        )

        scale(0.82)

        equipment {
            mainhand(Material.NETHERITE_SWORD)
            offhand(Material.AIR)
            setArmor(arrayOf(40, 32, 48), trim)
        }

    }

}