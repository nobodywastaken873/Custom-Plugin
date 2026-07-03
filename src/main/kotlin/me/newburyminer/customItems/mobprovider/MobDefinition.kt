package me.newburyminer.customItems.mobprovider

import me.newburyminer.customItems.effects.AttributeData
import me.newburyminer.customItems.effects.CustomEffectType
import me.newburyminer.customItems.effects.EffectData
import me.newburyminer.customItems.entity.hiteffects.HitEffect
import me.newburyminer.customItems.entity.hiteffects.effect.CustomDamageApply
import me.newburyminer.customItems.entity.hiteffects.effect.CustomEffectApply
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.damage.DamageType
import org.bukkit.entity.EntityType
import kotlin.math.roundToInt

interface MobDefinition {
    val id: String
        get() = this::class.java.name

    fun build(ctx: MobContext): MobBuilder

    fun mob(type: EntityType, block: MobBuilder.() -> Unit): MobBuilder {
        val builder = MobBuilder(type)
        builder.block()
        return builder
    }

    fun linear(base: Double, rate: Double, ctx: MobContext): Double { return base + rate * ctx.difficulty }
    fun linear(base: Int, rate: Double, ctx: MobContext): Int { return (base + rate * ctx.difficulty).roundToInt() }
    fun linear(range: Pair<Double, Double>, ctx: MobContext): Double { return range.first + (range.second - range.first) / 30.0 * ctx.difficulty }
    fun linear(range: Pair<Int, Int>, ctx: MobContext): Int { return (range.first + (range.second - range.first) / 30.0 * ctx.difficulty).roundToInt() }

    fun damage(amount: Double, type: DamageType): CustomDamageApply {return CustomDamageApply(amount, type) }
    fun attribute(attribute: Attribute, amount: Double, operation: AttributeModifier.Operation, duration: Int): CustomEffectApply {
        return CustomEffectApply(CustomEffectType.ATTRIBUTE, EffectData(duration, AttributeData(amount, attribute, operation)))
    }
}