package me.newburyminer.customItems.entity.components

import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Mob
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityPotionEffectEvent
import kotlin.math.max

class DefaultEntityComponent(private val maxTargetRange: Double = 50.0): EntityComponent {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "maxTargetRange" to maxTargetRange,
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.DEFAULT_ENTITY_COMPONENT
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return DefaultEntityComponent(
                map["maxTargetRange"].asDouble(),
            )
        }
    }

    override fun onAdd(wrapper: EntityWrapper) {
        val entity = wrapper.entity as? LivingEntity ?: return
        entity.getAttribute(Attribute.MAX_ABSORPTION)?.baseValue = 2047.0
    }

    override fun registerListeners(wrapper: EntityWrapper) {
        register(EntityDamageByEntityEvent::class, wrapper.entity.uniqueId, { e ->
            e.entity == wrapper.entity &&
            (e.damageSource.causingEntity !is Player && e.damageSource.directEntity !is Player)
        },
        {e ->
            e.isCancelled = true
        })
    }

    private var heldTarget: Player? = null

    override fun tick(wrapper: EntityWrapper) {
        if (heldTarget != null) {
            (wrapper.entity as? Mob ?: return).target = heldTarget
        }

        if (wrapper.entity.ticksLived % 20 == 0) {
            val mob = wrapper.entity as? Mob ?: return
            val validTargets = getTargetablePlayers(wrapper)
            val closestPlayer = validTargets
                .minByOrNull { it.location.subtract(mob.location).length() } ?: return

            if (heldTarget == null) {
                heldTarget = closestPlayer
            }

            // If the closest player is closer than 1/5 of the max target range and the current target is >2/5 of that
            else if (heldTarget != closestPlayer &&
                closestPlayer.location.subtract(mob.location).length() < maxTargetRange * 1/5 &&
                (heldTarget?.location?.subtract(mob.location)?.length() ?: 0.0) > maxTargetRange * 2/5
            ) {
                heldTarget = closestPlayer
            }

        }
    }

    private fun getTargetablePlayers(wrapper: EntityWrapper): List<Player> {
        val mob = wrapper.entity as? Mob ?: return emptyList()

        return wrapper.entity.getNearbyEntities(50.0, 50.0, 50.0)
            .filterIsInstance<Player>()
            .filter {
                (mob.hasLineOfSight(it) || it == heldTarget) &&
                !it.isDead && it.isValid
            }
    }

    /*override fun handle(ctx: EntityEventContext, wrapper: EntityWrapper) {
        when (val e = ctx.event) {

            is EntityDamageByEntityEvent -> {
                if (e.damageSource.causingEntity is Player || e.damageSource.directEntity is Player) return
                e.isCancelled = true
            }

            is EntityPotionEffectEvent -> {
                if (e.cause !in arrayOf(EntityPotionEffectEvent.Cause.POTION_SPLASH, EntityPotionEffectEvent.Cause.AREA_EFFECT_CLOUD)) return
                e.isCancelled = true
            }

        }
    }*/
}