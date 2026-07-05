package me.newburyminer.customItems.mobprovider.mobs.surface

import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.ExplosiveGrenadeAbility
import org.bukkit.Material
import org.bukkit.entity.EntityType

object DustThrower: MobDefinition() {
    
    override val tier: MobTier = MobTier.STANDARD
    override val targetRange: Double = 56.0
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.ZOMBIE_VILLAGER) {
    
        ability(
            MeleeEffectAbility(
                damage(linear(25.0 to 50.0, ctx), CustomDamageType.MELEE_NO_CD),
                VanillaKnockbackApply()
            )
        )

        ability(
            ExplosiveGrenadeAbility(
                linear(13.0 to 16.0, ctx),
                6.0,
                Material.GRAY_CONCRETE_POWDER,
                linear(4.0 to 7.0, ctx),
                linear(2 to 4, ctx),
                linear(300 to 200, ctx)
            )
        )
    
        health(
            linear(60.0 to 120.0, ctx)
        )
    
        movementSpeed(
            linear(1.3 to 1.6, ctx)
        )
    
    }
    
}