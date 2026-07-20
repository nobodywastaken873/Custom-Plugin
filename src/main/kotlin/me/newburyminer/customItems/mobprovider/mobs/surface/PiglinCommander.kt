package me.newburyminer.customItems.mobprovider.mobs.surface

import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.SummonerAbility
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern

object PiglinCommander: MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.SURFACE
    override val trim: TrimPattern = TrimPattern.SENTRY
    override val tier: MobTier = MobTier.ELITE
    override val targetRange: Double = 60.0
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.ZOMBIFIED_PIGLIN) {

        ability(
            MeleeEffectAbility(
                damage(linear(30.0 to 60.0, ctx), CustomDamageType.MELEE),
                VanillaKnockbackApply()
            )
        )

        ability(
            SummonerAbility(
                linear(3 to 6, ctx),
                PiglinWarrior,
                linear(40 to 30, ctx),
                linear(200 to 180, ctx),
                ParticleTheme.SURFACE
            )
        )

        ability(
            SummonerAbility(
                linear(1 to 2, ctx),
                PiglinCommander,
                linear(60 to 50, ctx),
                linear(500 to 450, ctx),
                ParticleTheme.SURFACE
            )
        )

        health(
            linear(250.0 to 500.0, ctx)
        )

        movementSpeed(
            linear(0.7 to 0.9, ctx)
        )

        scale(1.3)
        
        equipment {
            mainhand(Material.STONE_SWORD)
            offhand(Material.BREEZE_ROD)
            setArmor(colorTheme.color, trim)
        }

    }

}