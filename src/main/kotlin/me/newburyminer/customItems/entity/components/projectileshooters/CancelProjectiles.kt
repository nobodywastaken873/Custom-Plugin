package me.newburyminer.customItems.entity.components.projectileshooters

import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.projectiles.CustomDamageProjectile
import org.bukkit.entity.Evoker
import org.bukkit.entity.Spellcaster
import org.bukkit.event.entity.EntitySpellCastEvent
import org.bukkit.event.entity.ProjectileLaunchEvent

class CancelProjectiles: EntityComponent {

    override fun serialize(): Map<String, Any> {
        return mapOf()
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.CANCEL_PROJECTILES
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return CancelProjectiles()
        }
    }

    override fun registerListeners(wrapper: EntityWrapper) {
        register(
            ProjectileLaunchEvent::class, wrapper.entity.uniqueId, { e ->
            e.entity.shooter == wrapper.entity &&
            e.entity.getTag<Boolean>("spellsummoned") != true
        },
        {e ->
            e.isCancelled = true
        })

        register(
        EntitySpellCastEvent::class, wrapper.entity.uniqueId, { e ->
            e.entity == wrapper.entity
        },
        {e ->
            e.isCancelled = true
        })
    }

    override fun tick(wrapper: EntityWrapper) {
        if (wrapper.entity !is Evoker) return
        wrapper.entity.spell = Spellcaster.Spell.NONE
    }

    /*override fun handle(ctx: EntityEventContext, wrapper: EntityWrapper) {
        when (val e = ctx.event) {

            is ProjectileLaunchEvent -> {
                e.isCancelled = true
            }

        }
    }*/
}