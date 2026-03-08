package me.newburyminer.customItems.entity.components.projectileshooters

import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import org.bukkit.entity.LingeringPotion
import org.bukkit.entity.Witch
import org.bukkit.event.entity.ProjectileLaunchEvent

class LingeringWitchShooter: EntityComponent {

    override fun serialize(): Map<String, Any> {
        return mapOf()
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.LINGERING_WITCH_SHOOTER
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return LingeringWitchShooter()
        }
    }

    override fun registerListeners(wrapper: EntityWrapper) {
        register(ProjectileLaunchEvent::class, wrapper.entity.uniqueId, { e ->
            e.entity.shooter == wrapper.entity
        },
        {e ->
            e.isCancelled = true
            val witch = e.entity.shooter as? Witch ?: return@register
            witch.launchProjectile(LingeringPotion::class.java, e.entity.velocity)
        })
    }

    /*override fun handle(ctx: EntityEventContext, wrapper: EntityWrapper) {
        when (val e = ctx.event) {

            is ProjectileLaunchEvent -> {
                e.isCancelled = true
                val witch = e.entity.shooter as? Witch ?: return
                witch.launchProjectile(LingeringPotion::class.java, e.entity.velocity)
            }

        }
    }*/
}