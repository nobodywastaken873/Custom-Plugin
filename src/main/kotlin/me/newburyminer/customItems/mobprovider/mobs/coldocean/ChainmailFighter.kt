package me.newburyminer.customItems.mobprovider.mobs.coldocean

import me.newburyminer.customItems.entity.components.spells.MagicMissileShooterComponent
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.entity.velocity.DelayedStartVelocity
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.HomingSystem
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.defensive.DamageShieldAbility
import me.newburyminer.customItems.mobprovider.ability.spell.SummonerAbility
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier.Operation
import org.bukkit.entity.EntityType
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern

object ChainmailFighter : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.COLD_OCEAN
    override val trim: TrimPattern = TrimPattern.SENTRY
	override val tier: MobTier = MobTier.ELITE
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.DROWNED) {

        ability(
            DamageShieldAbility(
                linear(2 to 4, ctx),
                linear(80 to 50, ctx),
                ParticleTheme.COLD_OCEAN
            )
        )

        component(
            MagicMissileShooterComponent(
                linear(15.0 to 20.0, ctx),
                0.25,
                DelayedStartVelocity(0.15, 0.03, 0.05, HomingSystem.Type.BOTH_SCALED, 40),
                HitEffects(attribute(Attribute.JUMP_STRENGTH, -1.0, Operation.ADD_SCALAR, linear(60 to 100, ctx))),
                linear(40 to 25, ctx),
                linear(400 to 250, ctx),
                ParticleTheme.COLD_OCEAN
            )
        )

        ability(
            SummonerAbility(
                linear(2 to 4, ctx),
                FattenedMaggot,
                linear(40 to 30, ctx),
                linear(250 to 150, ctx),
                ParticleTheme.COLD_OCEAN
            )
        )

        ability(
            MeleeEffectAbility(
                damage(linear(27.0 to 54.0, ctx), CustomDamageType.MELEE),
                VanillaKnockbackApply()
            )
        )

        health(
            linear(240.0 to 480.0, ctx)
        )

        movementSpeed(
            linear(0.8 to 1.1, ctx)
        )

        attribute(Attribute.KNOCKBACK_RESISTANCE, 1.0, Operation.ADD_NUMBER)

        equipment {
            mainhand(Material.IRON_SWORD)
            offhand(Material.BLAZE_ROD)
            chest(Material.CHAINMAIL_CHESTPLATE, trim)
            legs(Material.CHAINMAIL_LEGGINGS, trim)
        }

    }

}