package me.newburyminer.customItems.entity.components.creepers

import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityEventContext
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.components.projectiles.ExplosiveProjectile
import me.newburyminer.customItems.entity.components.utils.DetonationInterface
import org.bukkit.entity.Creeper
import org.bukkit.event.entity.EntityExplodeEvent

class CustomExplosionCreeper(private val power: Float, private val setFire: Boolean, private val breakBlocks: Boolean = false): EntityComponent, DetonationInterface {
    override val componentType: EntityComponentType = EntityComponentType.CUSTOM_EXPLOSION_CREEPER

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "power" to power,
            "setfire" to setFire,
            "breakblocks" to breakBlocks
        )
    }
    override fun deserialize(map: Map<String, Any>): EntityComponent {
        val newPower = map["power"] as Float
        val newSetFire = map["setfire"] as Boolean
        val newBreakBlocks = map["breakblocks"] as Boolean
        return ExplosiveProjectile(newPower, newSetFire, newBreakBlocks)
    }

    override fun handle(ctx: EntityEventContext, wrapper: EntityWrapper) {
        when (val e = ctx.event) {

            is EntityExplodeEvent -> {
                e.isCancelled = true
                val creeper = e.entity as? Creeper ?: return
                creeper.clearActivePotionEffects()
                detonate(e.entity, power, setFire, breakBlocks)
            }

        }
    }
}