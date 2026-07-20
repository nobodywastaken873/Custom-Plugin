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
import me.newburyminer.customItems.mobprovider.ability.spell.TeleportBehindAbility
import org.bukkit.entity.EntityType

object CaveAssassin: MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.CAVES
    override val tier: MobTier = MobTier.STANDARD
    override val targetRange: Double = 65.0
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.ENDERMAN) {

        ability(
            MeleeEffectAbility(
                damage(linear(32.0 to 64.0, ctx), CustomDamageType.MELEE),
                VanillaKnockbackApply()
            )
        )

        ability(
            TeleportBehindAbility(
                linear(30.0 to 40.0, ctx),
                linear(300 to 250, ctx),
                linear(40 to 30, ctx),
            )
        )

        health(
            linear(80.0 to 160.0, ctx)
        )

        movementSpeed(
            linear(1.5 to 1.9, ctx)
        )

    }

}