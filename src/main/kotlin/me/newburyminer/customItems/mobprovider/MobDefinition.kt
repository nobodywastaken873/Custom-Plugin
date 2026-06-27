package me.newburyminer.customItems.mobprovider

import org.bukkit.entity.EntityType
import kotlin.math.roundToInt

interface MobDefinition {
    fun build(ctx: MobContext): MobBuilder

    fun mob(type: EntityType, block: MobBuilder.() -> Unit): MobBuilder {
        val builder = MobBuilder(type)
        builder.block()
        return builder
    }

    fun linear(base: Double, rate: Double, ctx: MobContext): Double {
        return base + rate * ctx.difficulty
    }

    fun linear(base: Int, rate: Double, ctx: MobContext): Int {
        return (base + rate * ctx.difficulty).roundToInt()
    }
}