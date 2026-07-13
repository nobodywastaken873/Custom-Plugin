package me.newburyminer.customItems.mobprovider.mobs.blackstone

import me.newburyminer.customItems.entity.hiteffects.effect.ProjectileKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.projectile.ProjectileEffectAbility
import me.newburyminer.customItems.mobprovider.ability.projectile.ProjectileHomingAbility
import org.bukkit.entity.EntityType

object BlazingTurret : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.BLACKSTONE
	override val tier: MobTier = MobTier.GRUNT
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.BLAZE) {

        ability(
            ProjectileEffectAbility(
                damage(linear(26.0 to 52.0, ctx), CustomDamageType.BURNING_NO_CD),
                ProjectileKnockbackApply()
            )
        )

        ability(
            ProjectileHomingAbility()
        )

        health(
            linear(36.0 to 72.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.2, ctx)
        )

    }

}