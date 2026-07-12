package me.newburyminer.customItems.mobprovider.mobs.military

import me.newburyminer.customItems.Utils.Companion.ench
import me.newburyminer.customItems.entity.hiteffects.effect.ExplosionApply
import me.newburyminer.customItems.entity.hiteffects.effect.ProjectileKnockbackApply
import me.newburyminer.customItems.entity.hiteffects.effect.SummonMobsApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.projectile.ProjectileEffectAbility
import me.newburyminer.customItems.mobprovider.ability.projectile.ProjectileHomingAbility
import me.newburyminer.customItems.mobprovider.ability.spell.BeamShooterAbility
import me.newburyminer.customItems.mobprovider.ability.spell.EffectMissileAbility
import me.newburyminer.customItems.mobprovider.mobs.mystic.UnstableHornet
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack

object AntiTankPersonnel : MobDefinition() {

	override val tier: MobTier = MobTier.ELITE
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.PILLAGER) {

        ability(
            ProjectileEffectAbility(
                damage(linear(24.0 to 48.0, ctx), CustomDamageType.PROJECTILE_NO_CD),
                ProjectileKnockbackApply()
            )
        )

        ability(
            ProjectileHomingAbility()
        )

        ability(
            EffectMissileAbility(
                linear(20.0 to 30.0, ctx),
                0.05,
                linear(0.3 to 0.5, ctx),
                linear(40 to 30, ctx),
                linear(150 to 120, ctx),
                ParticleTheme.MILITARY,
                ExplosionApply(linear(2.5 to 5.0, ctx).toFloat(), true)
            )
        )

        ability(
            BeamShooterAbility(
                linear(25.0 to 40.0, ctx),
                true,
                linear(60 to 50, ctx),
                linear(150 to 120, ctx),
                ParticleTheme.MILITARY,
                damage(linear(28.0 to 56.0, ctx), CustomDamageType.PROJECTILE_NO_CD)
            )
        )

        health(
            linear(250.0 to 500.0, ctx)
        )

        movementSpeed(
            linear(0.7 to 1.1, ctx)
        )

        equipment {
            mainhand(ItemStack(Material.CROSSBOW).ench("QC2"))
        }

    }

}