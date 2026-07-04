package me.newburyminer.customItems.mobprovider.mobs.warmocean

import me.newburyminer.customItems.entity.components.spells.LeapComponent
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.defensive.BasicDodgeAbility
import me.newburyminer.customItems.mobprovider.ability.spell.SummonerAbility
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType

object MutatedShark : MobDefinition() {

	override val tier: MobTier = MobTier.MINIBOSS
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.WARDEN) {

        ability(
            MeleeEffectAbility(
                damage(linear(32.0 to 64.0, ctx), CustomDamageType.MELEE_NO_CD),
                attribute(
                    Attribute.JUMP_STRENGTH,
                    -0.08,
                    AttributeModifier.Operation.ADD_SCALAR,
                    linear(40 to 60, ctx)
                ),
                VanillaKnockbackApply()
            )
        )

        component(
            LeapComponent(
                linear(10.0 to 14.0, ctx),
                0.8,
                linear(80 to 60, ctx),
            )
        )

        ability(
            BasicDodgeAbility(
                linear(0.15 to 0.3, ctx),
            )
        )

        ability(
            SummonerAbility(
                linear(2 to 3, ctx),
                EnragedSeaBeast,
                linear(50 to 40, ctx),
                linear(300 to 250, ctx),
                ParticleTheme.WARM_OCEAN
            )
        )

        ability(
            SummonerAbility(
                linear(2 to 3, ctx),
                SeaweedWrapper,
                linear(30 to 20, ctx),
                linear(300 to 200, ctx),
                ParticleTheme.WARM_OCEAN
            )
        )

        health(
            linear(800.0 to 1600.0, ctx)
        )

        movementSpeed(
            linear(1.3 to 1.5, ctx)
        )

    }

}