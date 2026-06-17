package me.newburyminer.customItems.entity.components.spells

import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.components.utils.CooldownInterface
import org.bukkit.Bukkit
import org.bukkit.entity.Mob
import kotlin.math.pow

class TeleportBehindComponent(private val baseCooldown: Int): EntityComponent, CooldownInterface {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "cooldown" to cooldown
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.TELEPORT_BEHIND
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return TeleportBehindComponent(
                map["cooldown"].toInt(),
            )
        }
    }

    override var cooldown: Int = 100
    override fun tick(wrapper: EntityWrapper) {
        if (Bukkit.getCurrentTick() % 20 == 0) {

            reduceCooldown(20)
            if (!offCooldown()) return
            val mob = wrapper.entity as? Mob ?: return
            val target = mob.target ?: return

            if (mob.location.subtract(target.location).lengthSquared() > 20.0.pow(2)) return
            val teleportVector = target.location.direction
                .setY(0)
                .normalize()
                .multiply(-2)

            val teleportLocation = target.location.add(teleportVector)
            if (
                    !teleportLocation.block.isPassable ||
                    !teleportLocation.clone().add(0.0, 1.0, 0.0).block.isPassable
                ) return

            mob.teleport(teleportLocation)
            applyCooldown(baseCooldown)

        }
    }
}