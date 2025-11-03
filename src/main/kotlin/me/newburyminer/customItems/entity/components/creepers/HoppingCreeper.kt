package me.newburyminer.customItems.entity.components.creepers

import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityEventContext
import me.newburyminer.customItems.entity.EntityWrapper
import org.bukkit.GameEvent
import org.bukkit.Location
import org.bukkit.entity.Creeper
import org.bukkit.event.world.GenericGameEvent
import org.bukkit.util.Vector

class HoppingCreeper: EntityComponent {
    override val componentType: EntityComponentType = EntityComponentType.HOPPING_CREEPER

    override fun serialize(): Map<String, Any> {
        return mapOf()
    }
    override fun deserialize(map: Map<String, Any>): EntityComponent {
        return HoppingCreeper()
    }

    override fun handle(ctx: EntityEventContext, wrapper: EntityWrapper) {
        when (val e = ctx.event) {

            is GenericGameEvent -> {
                if (e.event != GameEvent.PRIME_FUSE) return
                ticksUntilJump = (e.entity as Creeper).maxFuseTicks / 2 + 1
            }

        }
    }

    private var ticksUntilJump = 0
    override fun tick(wrapper: EntityWrapper) {
        if (ticksUntilJump <= 0) return
        if (ticksUntilJump == 1) {
            // activate jump
            val mob = wrapper.entity as? Creeper? ?: return
            val target = mob.target ?: return

            val toPlayer = target.location.subtract(mob.location).toVector()
            mob.velocity = toPlayer
                .multiply(0.21)
                .add(Vector(0.0, 0.6, 0.0))
                .add(target.velocity)

        }
        ticksUntilJump--
    }
}