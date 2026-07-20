package me.newburyminer.customItems.mobprovider.mobs.bosses.warden

import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern

object WardenZombie: MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.WARDEN
    override val trim: TrimPattern = TrimPattern.RIB
    override val tier: MobTier = MobTier.GRUNT
    override val targetRange: Double = 50.0
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.ZOMBIE) {

        ability(
            MeleeEffectAbility(
                damage(linear(14.0 to 28.0, ctx), CustomDamageType.MELEE),
                VanillaKnockbackApply()
            )
        )

        health(
            linear(25.0 to 50.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.3, ctx)
        )

        equipment {
            mainhand(Material.DIAMOND_SWORD)
            offhand(Material.AIR)
            setArmor(colorTheme.color, trim)
        }

    }

}