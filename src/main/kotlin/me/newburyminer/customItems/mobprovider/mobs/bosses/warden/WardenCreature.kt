package me.newburyminer.customItems.mobprovider.mobs.bosses.warden

import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.defensive.BasicDodgeAbility
import org.bukkit.entity.EntityType
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern

object WardenCreature: MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.WARDEN
    override val tier: MobTier = MobTier.GRUNT
    override val targetRange: Double = 50.0
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.ENDERMAN) {

        ability(
            MeleeEffectAbility(
                damage(linear(20.0 to 40.0, ctx), CustomDamageType.MELEE_NO_CD),
                VanillaKnockbackApply()
            )
        )

        ability(
            BasicDodgeAbility(
                linear(0.1 to 0.3, ctx),
            )
        )

        health(
            linear(27.0 to 54.0, ctx)
        )

        movementSpeed(
            linear(1.3 to 1.6, ctx)
        )

    }

}