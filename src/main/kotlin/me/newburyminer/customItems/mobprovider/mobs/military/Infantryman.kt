package me.newburyminer.customItems.mobprovider.mobs.military

import me.newburyminer.customItems.Utils.Companion.ench
import me.newburyminer.customItems.entity.hiteffects.effect.ProjectileKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.projectile.ProjectileEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.ExplosiveGrenadeAbility
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack

object Infantryman : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.MILITARY
	override val tier: MobTier = MobTier.GRUNT
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.PILLAGER) {

        ability(
            ExplosiveGrenadeAbility(
                linear(10.0 to 15.0, ctx),
                2.5,
                Material.GRAY_CONCRETE_POWDER,
                linear(4.0 to 7.0, ctx),
                linear(1 to 2, ctx),
                linear(300 to 250, ctx)
            )
        )

        ability(
            ProjectileEffectAbility(
                damage(linear(22.0 to 44.0, ctx), CustomDamageType.PROJECTILE_NO_CD),
                ProjectileKnockbackApply()
            )
        )

        health(
            linear(35.0 to 70.0, ctx)
        )

        movementSpeed(
            linear(1.3 to 1.5, ctx)
        )

        equipment {
            mainhand(ItemStack(Material.CROSSBOW).ench("QC3"))
        }

    }

}