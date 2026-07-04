package me.newburyminer.customItems.mobprovider.mobs.desert

import me.newburyminer.customItems.entity.hiteffects.effect.CustomKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.SummonerAbility
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType
import org.bukkit.util.Vector

object TempleGolem : MobDefinition() {

	override val tier: MobTier = MobTier.STANDARD
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.IRON_GOLEM) {

        ability(
            MeleeEffectAbility(
                damage(linear(35.0 to 70.0, ctx), CustomDamageType.MELEE_NO_CD),
                attribute(Attribute.ATTACK_SPEED, -0.05, AttributeModifier.Operation.ADD_NUMBER, linear(20 to 40, ctx)),
                CustomKnockbackApply(Vector(0.5, 2.0, 0.5))
            )
        )

        ability(
            SummonerAbility(
                linear(2 to 4, ctx),
                ScarabBeetleBomber,
                linear(40 to 30, ctx),
                linear(300 to 250, ctx),
                ParticleTheme.DESERT
            )
        )

        health(
            linear(100.0 to 200.0, ctx)
        )

        movementSpeed(
            linear(1.1 to 1.4, ctx)
        )

        scale(
            0.75
        )

    }

}