package me.newburyminer.customItems.mobprovider.mobs.mystic

import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.EffectAuraApply
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
import me.newburyminer.customItems.mobprovider.ability.projectile.ProjectileHomingAbility
import me.newburyminer.customItems.mobprovider.ability.spell.EffectMissileAbility
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern
import org.bukkit.potion.PotionEffectType

object BreakingBombadier : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.MYSTIC
    override val trim: TrimPattern = TrimPattern.BOLT
	override val tier: MobTier = MobTier.STANDARD
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.BOGGED) {

        ability(
            ProjectileHomingAbility()
        )

        ability(
            ProjectileEffectAbility(
                damage(linear(20.0 to 40.0, ctx), CustomDamageType.PROJECTILE),
                attribute(Attribute.ARMOR, -1.0, AttributeModifier.Operation.ADD_NUMBER, linear(100 to 150, ctx)),
                ProjectileKnockbackApply()
            )
        )

        ability(
            EffectMissileAbility(
                linear(20.0 to 30.0, ctx),
                0.05,
                linear(1.5 to 2.5, ctx),
                linear(40 to 30, ctx),
                linear(200 to 150, ctx),
                ParticleTheme.MYSTIC,
                attribute(
                    Attribute.ARMOR_TOUGHNESS,
                    -2.0,
                    AttributeModifier.Operation.ADD_NUMBER,
                    linear(150 to 200, ctx)
                ),
            )
        )

        health(
            linear(70.0 to 140.0, ctx)
        )

        movementSpeed(
            linear(1.2 to 1.4, ctx)
        )

        equipment {
            mainhand(Material.BOW)
            offhand(Material.GUNPOWDER)
            setArmor(colorTheme.color, trim)
        }

    }

}