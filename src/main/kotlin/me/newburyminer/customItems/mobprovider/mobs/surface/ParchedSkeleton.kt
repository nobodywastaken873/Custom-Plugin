package me.newburyminer.customItems.mobprovider.mobs.surface

import me.newburyminer.customItems.entity.hiteffects.effect.ProjectileKnockbackApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaEffectApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.projectile.ProjectileEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.EffectMissileAbility
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern
import org.bukkit.potion.PotionEffectType

object ParchedSkeleton: MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.SURFACE
    override val trim: TrimPattern = TrimPattern.COAST
    override val targetRange: Double = 50.0
    override val tier: MobTier = MobTier.GRUNT
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.PARCHED) {

        ability(
            ProjectileEffectAbility(
                damage(linear(24.0 to 48.0, ctx), CustomDamageType.PROJECTILE_NO_CD),
                ProjectileKnockbackApply()
            )
        )

        ability(
            EffectMissileAbility(
                linear(30.0 to 40.0, ctx),
                0.05,
                linear(1.5 to 2.5, ctx),
                linear(40 to 30, ctx),
                linear(200 to 150, ctx),
                ParticleTheme.SURFACE,
                VanillaEffectApply(PotionEffectType.HUNGER, linear(200 to 300, ctx), linear(3 to 4, ctx)),
            )
        )

        health(
            linear(30.0 to 60.0, ctx)
        )

        movementSpeed(
            linear(1.3 to 1.6, ctx)
        )

        equipment {
            mainhand(Material.BOW)
            offhand(Material.BLAZE_ROD)
            setArmor(colorTheme.color, trim)
        }

    }

}