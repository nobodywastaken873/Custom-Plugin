package me.newburyminer.customItems.mobprovider.mobs.caves

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
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern

object HeavyZombie: MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.CAVES
    override val trim: TrimPattern = TrimPattern.SNOUT
    override val tier: MobTier = MobTier.STANDARD
    override val targetRange: Double = 50.0
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.ZOMBIE_VILLAGER) {

        ability(
            MeleeEffectAbility(
                damage(linear(25.0 to 50.0, ctx), CustomDamageType.MELEE_NO_CD),
                VanillaKnockbackApply()
            )
        )

        ability(
            BasicSlashAbility(
                linear(1 to 3, ctx),
                linear(22.0 to 44.0, ctx),
                linear(150 to 100, ctx),
                ParticleTheme.CAVES
            )
        )

        health(
            linear(200.0 to 400.0, ctx)
        )

        movementSpeed(
            linear(0.8 to 1.2, ctx)
        )

        scale(1.2)

        equipment {
            mainhand(Material.NETHERITE_SWORD)
            offhand(Material.AIR)
            setArmor(colorTheme.color, trim)
        }

    }

}