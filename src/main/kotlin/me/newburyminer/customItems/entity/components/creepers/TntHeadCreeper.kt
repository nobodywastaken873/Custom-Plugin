package me.newburyminer.customItems.entity.components.creepers

import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityEventContext
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.projectiles.TntHeadTnt
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.TNTPrimed
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityExplodeEvent
import java.util.UUID

class TntHeadCreeper(private val tnt: Entity, private val power: Float, private val breakBlocks: Boolean = false): EntityComponent {
    override val componentType: EntityComponentType = EntityComponentType.TNT_HEAD_CREEPER

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "tnt" to tnt.uniqueId.toString(),
            "power" to power,
            "breakblocks" to breakBlocks
        )
    }
    override fun deserialize(map: Map<String, Any>): EntityComponent? {
        val newTnt = Bukkit.getEntity(UUID.fromString(map["tnt"] as String)) ?: return null
        val newPower = map["power"] as Float
        val newBreakBlocks = map["breakblocks"] as Boolean
        return TntHeadCreeper(newTnt, newPower, newBreakBlocks)
    }

    override fun handle(ctx: EntityEventContext, wrapper: EntityWrapper) {
        when (val e = ctx.event) {

            is EntityExplodeEvent -> {
                if (e.entity.getTag<Boolean>("exploding") != true) return
                EntityWrapperManager.getWrapperorNew(tnt)
                    .addComponent(TntHeadTnt(
                        e.entity.y,
                        power,
                        breakBlocks
                    ))
            }
            is EntityDeathEvent -> {
                tnt.remove()
            }

        }
    }

    override fun tick(wrapper: EntityWrapper) {
        if (Bukkit.getCurrentTick() % 10 == 0) {
            val newTnt = tnt as? TNTPrimed ?: return
            newTnt.fuseTicks = 20
        }
    }
}