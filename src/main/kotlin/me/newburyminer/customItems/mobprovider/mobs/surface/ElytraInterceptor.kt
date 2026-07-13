package me.newburyminer.customItems.mobprovider.mobs.surface

import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.defensive.DamageShieldAbility
import me.newburyminer.customItems.mobprovider.ability.spell.PullingBeamAbility
import org.bukkit.entity.EntityType

object ElytraInterceptor: MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.SURFACE
    override val tier: MobTier = MobTier.ELITE
    override val targetRange: Double = 120.0
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.PHANTOM) {

        ability(
            PullingBeamAbility(
                linear(2.0 to 4.0, ctx),
                500,
                10,
                ParticleTheme.SURFACE,
                range = 80.0
            )
        )

        ability(
            DamageShieldAbility(
                1,
                100,
                ParticleTheme.SURFACE
            )
        )

        health(
            linear(120.0 to 240.0, ctx)
        )

        movementSpeed(
            linear(1.2 * 4 to 1.5 * 4, ctx)
        )

        scale(
            4.0
        )

        apply {
            val top = InterceptorPilot.build(ctx).createEntity(ctx)
            this.addPassenger(top)
        }

    }

}