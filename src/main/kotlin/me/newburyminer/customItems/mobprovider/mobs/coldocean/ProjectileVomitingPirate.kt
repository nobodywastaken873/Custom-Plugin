package me.newburyminer.customItems.mobprovider.mobs.coldocean

import me.newburyminer.customItems.entity.hiteffects.effect.VanillaEffectApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.DamageAuraCastAbility
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern
import org.bukkit.potion.PotionEffectType

object ProjectileVomitingPirate : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.COLD_OCEAN
    override val trim: TrimPattern = TrimPattern.VEX
	override val tier: MobTier = MobTier.STANDARD
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.DROWNED) {

        ability(
            DamageAuraCastAbility(
                linear(1.0 to 2.0, ctx),
                0.2,
                20,
                20,
                150,
                10,
                ParticleTheme.COLD_OCEAN,
                linear(50 to 30, ctx),
                linear(250 to 150, ctx),
                linear(8.0 to 12.0, ctx),
                damage(linear(15.0 to 30.0, ctx), CustomDamageType.BURNING),
            )
        )

        ability(
            MeleeEffectAbility(
                damage(linear(20.0 to 45.0, ctx), CustomDamageType.MELEE),
                VanillaEffectApply(PotionEffectType.POISON, linear(20 to 60, ctx), linear(2 to 3, ctx)),
                VanillaKnockbackApply()
            )
        )

        health(
            linear(60.0 to 120.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.2, ctx)
        )

        equipment {
            mainhand(Material.IRON_SWORD)
            offhand(Material.BLAZE_ROD)
            setArmor(colorTheme.color, trim)
        }

    }

}