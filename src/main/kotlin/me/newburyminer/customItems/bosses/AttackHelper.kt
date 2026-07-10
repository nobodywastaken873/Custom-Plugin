package me.newburyminer.customItems.bosses

import me.newburyminer.customItems.effects.AttributeData
import me.newburyminer.customItems.effects.CustomEffectType
import me.newburyminer.customItems.effects.EffectData
import me.newburyminer.customItems.entity.hiteffects.effect.CustomDamageApply
import me.newburyminer.customItems.entity.hiteffects.effect.CustomEffectApply
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.damage.DamageType
import org.bukkit.util.Vector
import kotlin.math.roundToInt

interface AttackHelper {

    fun getCorners(center: Vector, offset: Double): List<Vector> {
        return listOf(1 to 1, 1 to -1, -1 to -1, -1 to 1).map {
            center.clone().add(Vector(it.first * offset, 0.0, it.second * offset))
        }
    }

    fun linear(range: Pair<Double, Double>, ctx: AttackContext<*>, useScalingCycle: Boolean = true): Double {
        val cycle = if (useScalingCycle) ctx.scalingCycle else ctx.cycle
        // Factor must be from 0.0 to 1.0, should be 0.0 to 0.6 for difficulty = 1, should be 0.4 to 1.0 for difficulty = 2
        val factor = when (ctx.difficulty) {
            1 -> {cycle.toDouble() / (ctx.maxCycle - 1) * 0.6}
            2 -> {cycle.toDouble() / (ctx.maxCycle - 1) * 0.6 + 0.4}
            else -> 0.0
        }
        return range.first + (range.second - range.first) * factor
    }

    fun linear(range: Pair<Int, Int>, ctx: AttackContext<*>, useScalingCycle: Boolean = true): Int {
        val cycle = if (useScalingCycle) ctx.scalingCycle else ctx.cycle
        // Factor must be from 0.0 to 1.0, should be 0.0 to 0.6 for difficulty = 1, should be 0.4 to 1.0 for difficulty = 2
        val factor = when (ctx.difficulty) {
            1 -> {cycle.toDouble() / (ctx.maxCycle - 1) * 0.6}
            2 -> {cycle.toDouble() / (ctx.maxCycle - 1) * 0.6 + 0.4}
            else -> 0.0
        }
        return (range.first + (range.second - range.first) * factor).roundToInt()
    }

    fun damage(amount: Double, type: DamageType): CustomDamageApply {return CustomDamageApply(amount, type) }
    fun attribute(attribute: Attribute, amount: Double, operation: AttributeModifier.Operation, duration: Int): CustomEffectApply {
        return CustomEffectApply(CustomEffectType.ATTRIBUTE, EffectData(duration, AttributeData(amount, attribute, operation)))
    }

}