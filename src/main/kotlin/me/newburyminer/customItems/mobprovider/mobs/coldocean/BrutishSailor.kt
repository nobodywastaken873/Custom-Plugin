package me.newburyminer.customItems.mobprovider.mobs.coldocean

import me.newburyminer.customItems.entity.hiteffects.effect.VanillaEffectApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.BasicSlashAbility
import org.bukkit.entity.EntityType
import org.bukkit.potion.PotionEffectType

object BrutishSailor: MobDefinition {
    
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.WITHER_SKELETON) {
    
        ability(
            BasicSlashAbility(
                linear(2 to 4, ctx),
                linear(20.0 to 40.0, ctx),
                linear(300 to 200, ctx),
                ParticleTheme.COLD_OCEAN,
                VanillaEffectApply(
                    PotionEffectType.WITHER,
                    linear(40 to 80, ctx),
                    1
                )
            )
        )

        ability(
            MeleeEffectAbility(
                damage(linear(25.0 to 50.0, ctx), CustomDamageType.MELEE_NO_CD),
                VanillaKnockbackApply()
            )
        )
    
        health(
            linear(80.0 to 160.0, ctx)
        )
    
        movementSpeed(
            linear(1.0 to 1.4, ctx)
        )
    
    }
    
}