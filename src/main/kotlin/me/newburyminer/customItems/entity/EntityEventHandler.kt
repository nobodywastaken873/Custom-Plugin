package me.newburyminer.customItems.entity

import org.bukkit.GameEvent
import org.bukkit.entity.Entity
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.*
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.world.GenericGameEvent

class EntityEventHandler: Listener {
    companion object {

        private fun dispatch(
            entity: Entity,
            event: Event,
        ) {
            val wrapper = EntityWrapperManager.getWrapper(entity.uniqueId) ?: return
            wrapper.handle(EntityEventContext(entity, event))

        }
    }

    @EventHandler fun onAggro(e: EntityTargetEvent) {
        if (EntityWrapperManager.getWrapper(e.entity.uniqueId) == null) {return}
        if (e.reason != EntityTargetEvent.TargetReason.FORGOT_TARGET) return
        e.isCancelled = true
    }

    @EventHandler fun onDamage(e: EntityDamageByEntityEvent) {
        dispatch(e.entity, e)
        dispatch(e.damager, e)
    }

    @EventHandler fun onDeath(e: EntityDeathEvent) {
        dispatch(e.entity, e)
    }

    @EventHandler fun onProjectileLaunch(e: ProjectileLaunchEvent) {
        dispatch((e.entity.shooter as? Entity?: return), e)
    }

    @EventHandler fun onProjectileLand(e: ProjectileHitEvent) {
        dispatch(e.entity, e)
    }

    @EventHandler fun onEntityExplode(e: EntityExplodeEvent) {
        dispatch(e.entity, e)
    }

    @EventHandler fun genericGameEvent(e: GenericGameEvent) {
        if (e.event !in arrayOf(GameEvent.PRIME_FUSE)) return
        dispatch(e.entity ?: return, e)
    }

    @EventHandler fun playerInteractEntity(e: PlayerInteractAtEntityEvent) {
        dispatch(e.rightClicked, e)
    }

    @EventHandler fun onRemove(e: EntityRemoveEvent) {
        dispatch(e.entity, e)
    }

    @EventHandler fun onFireworkExplode(e: FireworkExplodeEvent) {
        dispatch(e.entity, e)
    }
}