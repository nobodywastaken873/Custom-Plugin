package me.newburyminer.customItems.mobprovider.mobs

import me.newburyminer.customItems.entity.hiteffects.HitEffect
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import org.bukkit.entity.EntityType

object BasicZombie : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.SURFACE
	override val tier: MobTier = MobTier.GRUNT
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.ZOMBIE) {

        ability(
            MeleeEffectAbility(
                damage(
                    linear(20.0, 1.0, ctx),
                    CustomDamageType.MELEE_NO_CD
                )
            )
        )

        health(
            linear(40.0, 1.5, ctx)
        )

        movementSpeed(
            linear(1.25 to 2.0, ctx)
        )

    }

}