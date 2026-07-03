package me.newburyminer.customItems.mobprovider.mobs.rocky

import me.newburyminer.customItems.entity.hiteffects.effect.CustomKnockbackApply
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.ability.projectile.MachineGunAbility
import me.newburyminer.customItems.mobprovider.ability.spell.EffectAuraAbility
import org.bukkit.entity.EntityType
import org.bukkit.util.Vector

object AncientDefender: MobDefinition {

    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.STRAY) {

        ability(
            MachineGunAbility(
                linear(5.0 to 10.0, ctx),
                linear(7 to 5, ctx),
                linear(15.0 to 18.0, ctx),
                0.0,
            )
        )

        ability(
            EffectAuraAbility(
                linear(4.0 to 6.0, ctx),
                1.0,
                ParticleTheme.ROCKY,
                10,
                CustomKnockbackApply(
                    Vector(linear(1.0 to 2.0, ctx), 0.5, linear(1.0 to 2.0, ctx))
                )
            )
        )

        health(
            linear(50.0 to 100.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.5, ctx)
        )

    }

}