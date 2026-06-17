package me.newburyminer.customItems.systems

import me.newburyminer.customItems.eventbus.EventRegistry
import me.newburyminer.customItems.eventbus.ListenerEntry
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.entity.ExplosionPrimeEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.inventory.EquipmentSlot

object TestingSystem {
    fun registerListeners() {

        /*EventRegistry.register(ListenerEntry(EntityExplodeEvent::class, { e ->
            true
        },
            {e ->
                println("explode")
            }))

        EventRegistry.register(ListenerEntry(EntityDamageByEntityEvent::class, { e ->
            true
        },
        {e ->
            println("damage")
        }))

        EventRegistry.register(ListenerEntry(ExplosionPrimeEvent::class, { e ->
            true
        },
            {e ->
                println("prime")
            }))*/

        /*EventRegistry.register(
            ListenerEntry(EntityDamageEvent::class,
            { e -> true },
            { e ->
                val victim = e.entity as? LivingEntity ?: return@ListenerEntry

                println(
                    "DamageEvent " +
                            "victim=${victim.name} " +
                            "cause=${e.cause} " +
                            "damage=${e.damage} " +
                            "cancelled=${e.isCancelled}"
                )
            }))

        EventRegistry.register(ListenerEntry(PlayerDeathEvent::class, { e ->
            true
        },
        {e ->
            println("death: ${e.damageSource.damageType}, ${e.damageSource.causingEntity}")
        }))

        EventRegistry.register(ListenerEntry(PlayerRespawnEvent::class, { e ->
            true
        },
            {e ->
                println("respawn")
            }))*/

    }
}