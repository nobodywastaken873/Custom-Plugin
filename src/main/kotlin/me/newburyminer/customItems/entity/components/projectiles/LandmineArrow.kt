package me.newburyminer.customItems.entity.components.projectiles

import com.destroystokyo.paper.ParticleBuilder
import me.newburyminer.customItems.entity.*
import org.bukkit.Particle
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.Arrow
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityRemoveEvent
import org.bukkit.event.entity.ProjectileHitEvent

class LandmineArrow: EntityComponent {

    override fun serialize(): Map<String, Any> {
        return mapOf()
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.LANDMINE_ARROW
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return LandmineArrow()
        }
    }

    override fun registerListeners(wrapper: EntityWrapper) {
        register(EntityRemoveEvent::class, wrapper.entity.uniqueId, { e ->
            e.entity == wrapper.entity &&
            e.cause == EntityRemoveEvent.Cause.DESPAWN
        },
        {e ->
            val arrow = e.entity.world.spawn(e.entity.location, Arrow::class.java, CreatureSpawnEvent.SpawnReason.CUSTOM) {
                it.color = (e.entity as Arrow).color
                it.pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
            }
            arrow.shooter = (e.entity as Arrow).shooter
            EntityWrapperManager.getWrapperorNew(arrow)
                .addComponent(LandmineArrow())
        })

        register(ProjectileHitEvent::class, wrapper.entity.uniqueId, { e ->
            e.entity == wrapper.entity
        },
        {e ->
            e.isCancelled = true
        })
    }

    override fun tick(wrapper: EntityWrapper) {
        if (wrapper.entity.ticksLived % 4 == 0) {
            ParticleBuilder(Particle.SMOKE)
                .location(wrapper.entity.location.add(0.0, 0.3 + wrapper.entity.height, 0.0))
                .receivers(60)
                .spawn()
        }
    }

    /*override fun handle(ctx: EntityEventContext, wrapper: EntityWrapper) {
        when (val e = ctx.event) {

            is EntityRemoveEvent -> {
                if (e.cause != EntityRemoveEvent.Cause.DESPAWN) return
                val arrow = e.entity.world.spawn(e.entity.location, Arrow::class.java, CreatureSpawnEvent.SpawnReason.CUSTOM) {
                    it.color = (e.entity as Arrow).color
                    it.pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
                }
                arrow.shooter = (e.entity as Arrow).shooter
                EntityWrapperManager.getWrapperorNew(arrow)
                    .addComponent(LandmineArrow())
            }

            is ProjectileHitEvent -> {
                e.isCancelled = true
            }

        }
    }*/
}