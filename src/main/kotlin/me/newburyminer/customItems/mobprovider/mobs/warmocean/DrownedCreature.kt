package me.newburyminer.customItems.mobprovider.mobs.warmocean

import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.TeleportBehindAbility
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern

object DrownedCreature : MobDefinition() {

    override val trim: ArmorTrim = ArmorTrim(TrimMaterial.GOLD, TrimPattern.TIDE)
	override val tier: MobTier = MobTier.GRUNT
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.DROWNED) {

        ability(
            MeleeEffectAbility(
                damage(linear(23.0 to 46.0, ctx), CustomDamageType.MELEE_NO_CD),
                VanillaKnockbackApply()
            )
        )

        ability(
            TeleportBehindAbility(
                linear(20.0 to 30.0, ctx),
                linear(300 to 250, ctx),
                linear(40 to 35, ctx),
            )
        )

        health(
            linear(34.0 to 68.0, ctx)
        )

        movementSpeed(
            linear(0.85 to 1.2, ctx)
        )

        scale(0.9)

        equipment {
            mainhand(Material.DIAMOND_SWORD)
            offhand(Material.ENDER_PEARL)
            setArmor(arrayOf(49, 159, 181), trim)
        }

    }

}