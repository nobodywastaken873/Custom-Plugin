package me.newburyminer.customItems.mobprovider.mobs.warmocean

import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.BasicSlashAbility
import me.newburyminer.customItems.mobprovider.ability.spell.SummonerAbility
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern

object MassiveMermaid : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.WARM_OCEAN
    override val trim: TrimPattern = TrimPattern.SENTRY
	override val tier: MobTier = MobTier.ELITE
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.DROWNED) {

        ability(
            MeleeEffectAbility(
                damage(linear(28.0 to 56.0, ctx), CustomDamageType.MELEE),
                VanillaKnockbackApply()
            )
        )

        ability(
            SummonerAbility(
                linear(2 to 4, ctx),
                BabyMerman,
                linear(30 to 20, ctx),
                linear(250 to 200, ctx),
                ParticleTheme.WARM_OCEAN
            )
        )

        ability(
            BasicSlashAbility(
                linear(2 to 3, ctx),
                linear(20.0 to 40.0, ctx),
                linear(100 to 70, ctx),
                ParticleTheme.WARM_OCEAN
            )
        )

        health(
            linear(280.0 to 560.0, ctx)
        )

        movementSpeed(
            linear(0.9 to 1.3, ctx)
        )

        scale(1.2)

        equipment {
            mainhand(Material.DIAMOND_SWORD)
            offhand(Material.BREEZE_ROD)
            setArmor(colorTheme.color, trim)
        }

    }

}