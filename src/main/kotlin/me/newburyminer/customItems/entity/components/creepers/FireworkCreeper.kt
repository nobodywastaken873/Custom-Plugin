package me.newburyminer.customItems.entity.components.creepers

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityEventContext
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.projectiles.CustomDamageProjectile
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.CustomDamageApply
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.damage.DamageType
import org.bukkit.entity.Creeper
import org.bukkit.entity.Firework
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.util.Vector
import kotlin.math.pow

class FireworkCreeper(val count: Int, val damage: Double): EntityComponent {
    override val componentType: EntityComponentType = EntityComponentType.FIREWORK_CREEPER

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "count" to count,
            "damage" to damage
        )
    }
    override fun deserialize(map: Map<String, Any>): EntityComponent {
        val newCount = map["count"] as Int
        val newDamage = map["damage"] as Double
        return FireworkCreeper(newCount, newDamage)
    }

    override fun handle(ctx: EntityEventContext, wrapper: EntityWrapper) {
        when (val e = ctx.event) {

            is EntityExplodeEvent -> {
                if (e.entity.getTag<Boolean>("exploding") != true) return

                for (i in 1..count) {
                    val firework = e.entity.world.spawn(e.entity.location, Firework::class.java) {
                        it.isShotAtAngle = true
                        it.velocity = Vector(
                            Utils.randomRange(-1.0, 1.0),
                            Math.random(),
                            Utils.randomRange(-1.0, 1.0)
                        ).normalize().multiply(0.5)
                        it.ticksToDetonate = 5

                        val newMeta = it.fireworkMeta
                        val numStars = ((damage - 5) / 2).toInt()
                        val effect = FireworkEffect.builder()
                            .with(FireworkEffect.Type.BALL)
                            .withColor(Color.LIME)
                            .build()
                        for (i in 0..numStars) { newMeta.addEffect(effect) }
                        it.fireworkMeta = newMeta
                    }
                }

            }

        }
    }
}