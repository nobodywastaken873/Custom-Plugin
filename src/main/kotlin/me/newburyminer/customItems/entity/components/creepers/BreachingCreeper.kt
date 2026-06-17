package me.newburyminer.customItems.entity.components.creepers

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Creeper
import org.bukkit.entity.Player
import kotlin.math.pow

class BreachingCreeper(private val threshold: Double): EntityComponent {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "threshold" to threshold,
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.BREACHING_CREEPER
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return BreachingCreeper(map["threshold"].toDouble())
        }
    }

    private var prevLoc: Location? = null
    override fun tick(wrapper: EntityWrapper) {
        if (Bukkit.getCurrentTick() % 20 == 0) {
            val currentLoc = wrapper.entity.location
            val distanceSquared = currentLoc.distanceSquared(prevLoc ?: Location(wrapper.entity.world, 0.0, 0.0, 0.0))
            prevLoc = currentLoc.clone()
            if (distanceSquared < threshold.pow(2)) {

                val mob = wrapper.entity as? Creeper ?: return
                val player = mob.target as? Player ?: return
                mob.ignite()
                if (Math.random() < 0.25) {
                    mob.velocity = player.location.subtract(mob.location).toVector().normalize().multiply(Math.random() * 10)
                }

            }
        }
    }
}