package me.newburyminer.customItems.entity

import io.papermc.paper.command.brigadier.argument.ArgumentTypes.player
import org.bukkit.entity.Entity
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.entity.EntityTargetEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.ProjectileLaunchEvent

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

}