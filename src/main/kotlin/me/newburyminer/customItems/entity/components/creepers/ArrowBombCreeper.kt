package me.newburyminer.customItems.entity.components.creepers

import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityEventContext
import me.newburyminer.customItems.entity.EntityWrapper

class ArrowBombCreeper(val count: Int, val damage: Double): EntityComponent {
    override val componentType: EntityComponentType = EntityComponentType.ARROWBOMB_CREEPER

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "count" to count,
            "damage" to damage
        )
    }
    override fun deserialize(map: Map<String, Any>): EntityComponent {
        val newCount = map["count"] as Int
        val newDamage = map["damage"] as Double
        return ArrowBombCreeper(newCount, newDamage)
    }

    override fun handle(ctx: EntityEventContext, wrapper: EntityWrapper) {
        when (val e = ctx.event) {



        }
    }
}