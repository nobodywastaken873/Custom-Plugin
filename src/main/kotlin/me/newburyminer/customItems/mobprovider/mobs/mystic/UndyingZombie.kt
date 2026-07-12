package me.newburyminer.customItems.mobprovider.mobs.mystic

import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.DeathSummonAbility
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern

object UndyingZombie : MobDefinition() {

    override val trim: ArmorTrim = ArmorTrim(TrimMaterial.EMERALD, TrimPattern.WARD)
	override val tier: MobTier = MobTier.STANDARD
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.ZOMBIE_VILLAGER) {

        if (Math.random() < 0.9) {
            ability(
                DeathSummonAbility(
                    UndyingZombie,
                    1
                )
            )
        }

        ability(
            MeleeEffectAbility(
                damage(linear(25.0 to 50.0, ctx), CustomDamageType.MELEE_NO_CD),
                VanillaKnockbackApply()
            )
        )

        health(
            linear(50.0 to 100.0, ctx)
        )

        movementSpeed(
            linear(0.8 to 1.2, ctx)
        )

        equipment {
            mainhand(Material.WOODEN_SWORD)
            offhand(Material.AIR)
            setArmor(arrayOf(89, 70, 46), trim)
        }

    }

}