package me.newburyminer.customItems.entity.components.creepers

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Creeper
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.util.Vector

class FirebombCreeper(val rate: Double): EntityComponent {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "rate" to rate
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.FIREBOMB_CREEPER
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return FirebombCreeper(map["rate"].toDouble())
        }
    }

    override fun registerListeners(wrapper: EntityWrapper) {
        register(EntityExplodeEvent::class, wrapper.entity.uniqueId, { e ->
            e.entity == wrapper.entity
        },
        {e ->
            Bukkit.getScheduler().runTaskLater(CustomItems.plugin, Runnable {
                val radius = (e.entity as Creeper).explosionRadius
                val interval = -radius..radius

                for (x in interval) for (y in interval) for (z in interval) {
                    if (e.location.clone().add(Vector(x, y, z)).block.type == Material.FIRE)
                        if (Math.random() < rate) e.location.clone().add(Vector(x, y, z)).block.type = Material.LAVA
                }
            }, 1)
        })
    }

    /*override fun handle(ctx: EntityEventContext, wrapper: EntityWrapper) {
        when (val e = ctx.event) {

            is EntityExplodeEvent -> {
                if (e.entity.getTag<Boolean>("exploding") != true) return
                Bukkit.getScheduler().runTaskLater(CustomItems.plugin, Runnable {
                    val radius = (e.entity as Creeper).explosionRadius
                    val interval = -radius..radius

                    for (x in interval) for (y in interval) for (z in interval) {

                        if (e.location.clone().add(Vector(x, y, z)).block.type == Material.FIRE)
                            if (Math.random() < rate)
                                e.location.clone().add(Vector(x, y, z)).block.type = Material.LAVA

                    }
                }, 1)
            }

        }
    }*/
}