package me.newburyminer.customItems.mobprovider.mobs.military

import me.newburyminer.customItems.entity.hiteffects.effect.VanillaEffectApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.TeleportBehindAbility
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.potion.PotionEffectType

object ExperiencedAssassin : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.MILITARY
	override val tier: MobTier = MobTier.STANDARD
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.VINDICATOR) {

        ability(
            MeleeEffectAbility(
                damage(linear(32.0 to 64.0, ctx), CustomDamageType.MELEE),
                VanillaEffectApply(PotionEffectType.BLINDNESS, linear(30 to 50, ctx), 0),
                VanillaKnockbackApply()
            )
        )

        ability(
            TeleportBehindAbility(
                linear(25.0 to 35.0, ctx),
                linear(300 to 280, ctx),
                linear(40 to 30, ctx),
            )
        )

        health(
            linear(65.0 to 130.0, ctx)
        )

        movementSpeed(
            linear(1.3 to 1.5, ctx)
        )

        equipment {
            mainhand(Material.IRON_SWORD)
            offhand(Material.IRON_SWORD)
        }

    }

}