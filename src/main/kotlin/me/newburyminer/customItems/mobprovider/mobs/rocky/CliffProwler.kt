package me.newburyminer.customItems.mobprovider.mobs.rocky

import me.newburyminer.customItems.entity.hiteffects.effect.CustomKnockbackApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobFactory
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.DeathSummonAbility
import me.newburyminer.customItems.mobprovider.ability.spell.TrackingBeamAbility
import org.bukkit.entity.EntityType
import org.bukkit.util.Vector

object CliffProwler: MobDefinition {
    
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.RAVAGER) {
    
        ability(
            MeleeEffectAbility(
                damage(linear(30.0 to 60.0, ctx), CustomDamageType.MELEE_NO_CD),
                CustomKnockbackApply(Vector(1.2, 0.5, 1.2))
            )
        )

        ability(
            DeathSummonAbility(
                StoneThrower,
                linear(4 to 8, ctx),
            )
        )

        ability(
            TrackingBeamAbility(
                linear(40.0 to 80.0, ctx),
                10,
                400,
                10,
                ParticleTheme.ROCKY,
                damage(linear(8.0 to 16.0, ctx), CustomDamageType.MAGIC_NO_CD)
            )
        )

        health(
            linear(300.0 to 600.0, ctx)
        )
    
        movementSpeed(
            linear(1.2 to 1.5, ctx)
        )
        
        scale(0.85)

        apply {
            val rider = MobFactory.create(AncientDefender.build(ctx), ctx)
            this.addPassenger(rider)
        }

    }
    
}