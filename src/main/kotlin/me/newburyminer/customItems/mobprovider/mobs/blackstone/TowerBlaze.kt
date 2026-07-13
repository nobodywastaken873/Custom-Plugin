package me.newburyminer.customItems.mobprovider.mobs.blackstone

import me.newburyminer.customItems.entity.hiteffects.effect.ProjectileKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.projectile.ProjectileEffectAbility
import me.newburyminer.customItems.mobprovider.ability.projectile.ProjectileHomingAbility
import me.newburyminer.customItems.mobprovider.ability.spell.PullingBeamAbility
import org.bukkit.entity.EntityType

object TowerBlaze : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.BLACKSTONE
	override val tier: MobTier = MobTier.MINIBOSS
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.BLAZE) {

        ability(
            PullingBeamAbility(
                -1.0,
                400,
                10,
                ParticleTheme.BLACKSTONE,
            )
        )

        ability(
            ProjectileEffectAbility(
                damage(linear(26.0 to 52.0, ctx), CustomDamageType.BURNING_NO_CD),
                ProjectileKnockbackApply(0.2)
            )
        )

        ability(
            ProjectileHomingAbility()
        )

        health(
            linear(500.0 to 1000.0, ctx)
        )

        movementSpeed(
            linear(0.8 to 1.2, ctx)
        )

        scale(1.2)

        apply {
            val top = UpperTowerBlaze.build(ctx).createEntity(ctx)
            this.addPassenger(top)
        }

    }

}