package me.newburyminer.customItems.mobprovider.mobs.mystic

import me.newburyminer.customItems.entity.components.utils.ProjectileType
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.EffectAuraApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaEffectApply
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.defensive.BasicDodgeAbility
import me.newburyminer.customItems.mobprovider.ability.projectile.SniperProjectileAbility
import me.newburyminer.customItems.mobprovider.ability.spell.EffectAuraAbility
import me.newburyminer.customItems.mobprovider.ability.spell.EffectMissileAbility
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern
import org.bukkit.potion.PotionEffectType

object MysticSniper : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.MYSTIC
    override val trim: TrimPattern = TrimPattern.EYE
	override val tier: MobTier = MobTier.STANDARD
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.BOGGED) {

        ability(
            SniperProjectileAbility(
                linear(35.0 to 75.0, ctx),
                linear(200 to 180, ctx),
                linear(10 to 20, ctx),
                projectileType = ProjectileType.SHULKER_BULLET
            )
        )

        ability(
            EffectMissileAbility(
                linear(20.0 to 30.0, ctx),
                0.05,
                linear(1.5 to 2.5, ctx),
                linear(40 to 30, ctx),
                linear(150 to 100, ctx),
                ParticleTheme.MYSTIC,
                EffectAuraApply(
                    linear(3.0 to 4.0, ctx),
                    1.0,
                    linear(100 to 150, ctx),
                    HitEffects(
                        VanillaEffectApply(
                            PotionEffectType.SLOWNESS,
                            linear(40 to 60, ctx),
                            linear(3 to 6, ctx)
                        )
                    ),
                    20,
                    ParticleTheme.MYSTIC,
                )
            )
        )

        ability(
            BasicDodgeAbility(
                linear(0.1 to 0.2, ctx),
            )
        )

        health(
            linear(65.0 to 130.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.2, ctx)
        )

        equipment {
            mainhand(Material.BOW)
            offhand(Material.SPYGLASS)
            setArmor(colorTheme.color, trim)
        }

    }

}