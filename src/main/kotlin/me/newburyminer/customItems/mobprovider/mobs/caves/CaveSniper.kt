package me.newburyminer.customItems.mobprovider.mobs.caves

import me.newburyminer.customItems.entity.components.projectileshooters.CancelProjectiles
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.defensive.BasicDodgeAbility
import me.newburyminer.customItems.mobprovider.ability.spell.BeamShooterAbility
import org.bukkit.entity.EntityType

object CaveSniper: MobDefinition() {

    override val tier: MobTier = MobTier.STANDARD
    override val targetRange: Double = 60.0
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.SKELETON) {

        ability(
            BeamShooterAbility(
                linear(40.0 to 50.0, ctx),
                false,
                linear(40 to 30, ctx),
                linear(100 to 80, ctx),
                ParticleTheme.CAVES
            )
        )

        component(
            CancelProjectiles()
        )

        ability(
            BasicDodgeAbility(
                linear(0.2 to 0.4, ctx),
            )
        )

        health(
            linear(65.0 to 130.0, ctx)
        )

        movementSpeed(
            linear(0.8 to 1.2, ctx)
        )

    }

}