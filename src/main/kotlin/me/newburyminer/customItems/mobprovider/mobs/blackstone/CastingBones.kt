package me.newburyminer.customItems.mobprovider.mobs.blackstone

import me.newburyminer.customItems.entity.hiteffects.effect.DisableShieldApply
import me.newburyminer.customItems.entity.hiteffects.effect.ProjectileKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.projectile.ProjectileEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.EffectMissileAbility
import me.newburyminer.customItems.mobprovider.ability.spell.MultiMissileAbility
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern

object CastingBones : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.BLACKSTONE
    override val trim: TrimPattern = TrimPattern.VEX
	override val tier: MobTier = MobTier.GRUNT
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.SKELETON) {

        ability(
            MultiMissileAbility(
                linear(25.0 to 35.0, ctx),
                linear(2 to 4, ctx),
                linear(24.0 to 48.0, ctx),
                ProjectileKnockbackApply(),
                linear(2.0 to 3.0, ctx),
                linear(25 to 15, ctx),
                linear(200 to 180, ctx),
                ParticleTheme.BLACKSTONE
            )
        )

        ability(
            ProjectileEffectAbility(
                damage(linear(24.0 to 48.0, ctx), CustomDamageType.PROJECTILE),
                ProjectileKnockbackApply()
            )
        )

        ability(
            EffectMissileAbility(
                linear(25.0 to 35.0, ctx),
                0.05,
                linear(2.0 to 3.0, ctx),
                linear(30 to 20, ctx),
                linear(200 to 120, ctx),
                ParticleTheme.BLACKSTONE,
                DisableShieldApply(ignoreDirection = true),
                damage(linear(25.0 to 50.0, ctx), CustomDamageType.BURNING)
            )
        )

        health(
            linear(40.0 to 80.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.5, ctx)
        )

        equipment {
            mainhand(Material.BOW)
            offhand(Material.BLAZE_ROD)
            setArmor(colorTheme.color, trim)
        }

    }

}