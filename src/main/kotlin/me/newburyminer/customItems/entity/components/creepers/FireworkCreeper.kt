package me.newburyminer.customItems.entity.components.creepers

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.entity.Firework
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.util.Vector

class FireworkCreeper(val count: Int, val damage: Double): EntityComponent {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "count" to count,
            "damage" to damage
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.FIREWORK_CREEPER
        override fun deserialize(map: Map<String, Any>): EntityComponent {
        val newCount = map["count"].asInt()
        val newDamage = map["damage"].asDouble()
        return FireworkCreeper(newCount, newDamage)
    }
    }

    override fun registerListeners(wrapper: EntityWrapper) {
        register(EntityExplodeEvent::class, wrapper.entity.uniqueId, { e ->
            e.entity == wrapper.entity
        },
        {e ->
            for (i in 1..count) {
                val firework = e.entity.world.spawn(e.entity.location, Firework::class.java) {
                    it.isShotAtAngle = true
                    it.velocity = Vector(Utils.randomRange(-1.0, 1.0), Math.random(), Utils.randomRange(-1.0, 1.0)).normalize().multiply(0.5)
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
        })
    }

    /*override fun handle(ctx: EntityEventContext, wrapper: EntityWrapper) {
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
    }*/
}