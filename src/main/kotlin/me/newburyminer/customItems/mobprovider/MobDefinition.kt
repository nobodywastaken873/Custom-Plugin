package me.newburyminer.customItems.mobprovider

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.effects.AttributeData
import me.newburyminer.customItems.effects.CustomEffectType
import me.newburyminer.customItems.effects.EffectData
import me.newburyminer.customItems.entity.hiteffects.effect.CustomDamageApply
import me.newburyminer.customItems.entity.hiteffects.effect.CustomEffectApply
import me.newburyminer.customItems.helpers.copyTo
import me.newburyminer.customItems.structures.StructureReference
import org.bukkit.Location
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.damage.DamageType
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern
import org.bukkit.util.BoundingBox
import kotlin.math.roundToInt
import kotlin.reflect.KClass

abstract class MobDefinition : SpawnOption {
    val id: String
        get() = this::class.java.name
    abstract val tier: MobTier
    open val targetRange: Double
        get() = 40.0
    open val trim: ArmorTrim? = null

    abstract fun build(ctx: MobContext): MobBuilder

    fun mob(type: EntityType, block: MobBuilder.() -> Unit): MobBuilder {
        val builder = MobBuilder(type, tier, targetRange, trim ?: ArmorTrim(TrimMaterial.IRON, TrimPattern.RIB))
        builder.block()
        return builder
    }

    private var box: BoundingBox? = null
    fun getHitbox(center: Location = Location(null, 0.0, 0.0, 0.0)): BoundingBox {
        if (box != null) return box!!.copyTo(center.toVector())

        val context = MobContext(0.1, StructureReference.Difficulty.NORMAL,
            Location(CustomItems.aridWorld, 0.0, 1000.0, 0.0))
        val testMob = build(context).createEntity(context)
        box = testMob.boundingBox
        testMob.remove()
        return box!!.copyTo(center.toVector())
    }

    private var type: KClass<out LivingEntity>? = null
    fun getType(): KClass<out LivingEntity> {
        if (type != null) return type!!

        val context = MobContext(0.1, StructureReference.Difficulty.NORMAL,
            Location(CustomItems.aridWorld, 0.0, 1000.0, 0.0))
        val testMob = build(context).createEntity(context)
        type = testMob::class
        testMob.remove()
        return type!!
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