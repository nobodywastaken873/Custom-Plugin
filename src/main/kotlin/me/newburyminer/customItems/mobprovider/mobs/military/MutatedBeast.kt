package me.newburyminer.customItems.mobprovider.mobs.military

import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.BasicSlashAbility
import me.newburyminer.customItems.mobprovider.ability.spell.EffectMissileAbility
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType

object MutatedBeast : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.MILITARY
	override val tier: MobTier = MobTier.MINIBOSS
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.RAVAGER) {

        ability(
            BasicSlashAbility(
                linear(2 to 4, ctx),
                linear(24.0 to 48.0, ctx),
                linear(140 to 100, ctx),
                ParticleTheme.MILITARY,
            )
        )

        ability(
            MeleeEffectAbility(
                damage(linear(30.0 to 60.0, ctx), CustomDamageType.MELEE),
                VanillaKnockbackApply()
            )
        )

        ability(
            EffectMissileAbility(
                linear(20.0 to 30.0, ctx),
                0.05,
                linear(0.4 to 0.6, ctx),
                linear(40 to 30, ctx),
                linear(150 to 120, ctx),
                ParticleTheme.MILITARY,
                attribute(Attribute.MOVEMENT_SPEED, -1.0, AttributeModifier.Operation.MULTIPLY_SCALAR_1, 20),
                attribute(Attribute.MOVEMENT_SPEED, -1.0, AttributeModifier.Operation.MULTIPLY_SCALAR_1, 20),
            )
        )

        health(
            linear(500.0 to 1000.0, ctx)
        )

        movementSpeed(
            linear(1.4 to 1.8, ctx)
        )

        apply {
            val top = CavalryRider.build(ctx).createEntity(ctx)
            this.addPassenger(top)
        }

    }

}