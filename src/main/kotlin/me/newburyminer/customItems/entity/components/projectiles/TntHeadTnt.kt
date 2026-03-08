package me.newburyminer.customItems.entity.components.projectiles

import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityEventContext
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.components.utils.DetonationInterface
import me.newburyminer.customItems.helpers.DoubleRange
import org.bukkit.Bukkit
import org.bukkit.entity.TNTPrimed
import org.bukkit.event.entity.EntityExplodeEvent

class TntHeadTnt(private val explodeY: Double, private val power: Float, private val breakBlocks: Boolean = false): EntityComponent, DetonationInterface {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "explodey" to explodeY,
            "power" to power,
            "breakblocks" to breakBlocks
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.TNT_HEAD_TNT
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            val newExplodeY = map["explodey"].toDouble()
            val newPower = map["power"].toFloat()
            val newBreakBlocks = map["breakblocks"].toBoolean()
            return TntHeadTnt(newExplodeY, newPower, newBreakBlocks)
        }
    }

    override fun registerListeners(wrapper: EntityWrapper) {
        register(EntityExplodeEvent::class, wrapper.entity.uniqueId, { e ->
            e.entity == wrapper.entity
        },
        {e ->
            e.isCancelled = true
            detonate(wrapper.entity, power, false, breakBlocks)
        })
    }

    /*override fun handle(ctx: EntityEventContext, wrapper: EntityWrapper) {
        when (val e = ctx.event) {

            is EntityExplodeEvent -> {
                e.isCancelled = true
                detonate(wrapper.entity, power, false, breakBlocks)
            }

        }
    }*/

    private var counter = 0
    override fun tick(wrapper: EntityWrapper) {
        if (Bukkit.getCurrentTick() % 5 == 0) {
            val tnt = wrapper.entity as? TNTPrimed ?: return
            tnt.fuseTicks = 20
            counter++

            if (tnt.y in DoubleRange(explodeY - 0.5, explodeY + 0.5) || counter > 20)
                tnt.fuseTicks = 0
        }
    }
}