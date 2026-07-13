package me.newburyminer.customItems.mobprovider.mobs.warmocean

import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.defensive.LeapDodgeAbility
import me.newburyminer.customItems.mobprovider.ability.spell.EffectMissileAbility
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType
import org.bukkit.entity.MagmaCube

object UnderseaAbomination : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.WARM_OCEAN
	override val tier: MobTier = MobTier.STANDARD
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.SLIME) {

        ability(
            EffectMissileAbility(
                linear(20.0 to 30.0, ctx),
                0.05,
                linear(0.4 to 0.7, ctx),
                linear(40 to 30, ctx),
                linear(200 to 150, ctx),
                ParticleTheme.WARM_OCEAN,
                damage(linear(35.0 to 70.0, ctx), CustomDamageType.PROJECTILE_NO_CD),
                VanillaKnockbackApply()
            )
        )

        ability(
            MeleeEffectAbility(
                damage(linear(20.0 to 40.0, ctx), CustomDamageType.MELEE_NO_CD),
                VanillaKnockbackApply()
            )
        )

        ability(
            LeapDodgeAbility(
                linear(0.25 to 0.4, ctx),
                linear(100 to 80, ctx),
                linear(1.2 to 2.0, ctx),
            )
        )

        health(
            linear(120.0 to 240.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.2, ctx)
        )

        apply {
            if (this !is MagmaCube) return@apply
            this.size = 1
        }

        scale(2.5)

    }

}