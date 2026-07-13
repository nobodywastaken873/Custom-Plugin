package me.newburyminer.customItems.mobprovider.mobs.rocky

import me.newburyminer.customItems.entity.hiteffects.effect.ProjectileKnockbackApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaEffectApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.projectile.ProjectileEffectAbility
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern
import org.bukkit.potion.PotionEffectType

object LeadenSkeleton : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.ROCKY
    override val trim: TrimPattern = TrimPattern.COAST
	override val tier: MobTier = MobTier.GRUNT
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.STRAY) {

        ability(
            ProjectileEffectAbility(
                damage(linear(24.0 to 48.0, ctx), CustomDamageType.PROJECTILE_NO_CD),
                VanillaEffectApply(PotionEffectType.WITHER, linear(30 to 60, ctx), linear(1 to 2, ctx)),
                ProjectileKnockbackApply()
            )
        )

        health(
            linear(32.0 to 64.0, ctx)
        )

        movementSpeed(
            linear(0.9 to 1.2, ctx)
        )

        equipment {
            mainhand(Material.BOW)
            offhand(Material.AIR)
            setArmor(colorTheme.color, trim)
        }

    }

}