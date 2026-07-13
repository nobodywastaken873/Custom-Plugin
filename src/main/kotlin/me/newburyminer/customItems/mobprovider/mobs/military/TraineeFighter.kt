package me.newburyminer.customItems.mobprovider.mobs.military

import me.newburyminer.customItems.entity.hiteffects.effect.DisableShieldApply
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

object TraineeFighter : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.MILITARY
	override val tier: MobTier = MobTier.GRUNT
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.VINDICATOR) {

        ability(
            MeleeEffectAbility(
                damage(linear(28.0 to 56.0, ctx), CustomDamageType.MELEE_NO_CD),
                DisableShieldApply(),
                VanillaKnockbackApply()
            )
        )

        ability(
            BasicSlashAbility(
                1,
                linear(32.0 to 64.0, ctx),
                linear(120 to 100, ctx),
                ParticleTheme.MILITARY
            )
        )

        health(
            linear(35.0 to 70.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.2, ctx)
        )

        equipment {
            mainhand(Material.STONE_SWORD)
        }

    }

}