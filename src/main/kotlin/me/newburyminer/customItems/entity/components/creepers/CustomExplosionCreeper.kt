package me.newburyminer.customItems.entity.components.creepers

import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.components.projectiles.ExplosiveProjectile
import me.newburyminer.customItems.entity.components.utils.DetonationInterface
import org.bukkit.entity.Creeper
import org.bukkit.event.entity.ExplosionPrimeEvent

class CustomExplosionCreeper(private val power: Float, private val setFire: Boolean, private val breakBlocks: Boolean = false): EntityComponent, DetonationInterface {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "power" to power,
            "setfire" to setFire,
            "breakblocks" to breakBlocks
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.CUSTOM_EXPLOSION_CREEPER
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            val newPower = map["power"].asFloat()
            val newSetFire = map["setfire"].asBoolean()
            val newBreakBlocks = map["breakblocks"].asBoolean()
            return ExplosiveProjectile(newPower, newSetFire, newBreakBlocks)
        }
    }

    override fun registerListeners(wrapper: EntityWrapper) {
        register(ExplosionPrimeEvent::class, wrapper.entity.uniqueId, { e ->
            e.entity == wrapper.entity
        },
        {e ->
            e.isCancelled = true
            val creeper = e.entity as? Creeper ?: return@register
            creeper.clearActivePotionEffects()
            detonate(e.entity, power, setFire, breakBlocks)
        })
    }

    /*override fun handle(ctx: EntityEventContext, wrapper: EntityWrapper) {
        when (val e = ctx.event) {

            is EntityExplodeEvent -> {
                e.isCancelled = true
                val creeper = e.entity as? Creeper ?: return
                creeper.clearActivePotionEffects()
                detonate(e.entity, power, setFire, breakBlocks)
            }

        }
    }*/
}