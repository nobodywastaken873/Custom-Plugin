package me.newburyminer.customItems.entity.components.creepers

import com.google.common.base.Equivalence
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityEventContext
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.projectiles.CustomDamageProjectile
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.CustomDamageApply
import org.bukkit.damage.DamageType
import org.bukkit.entity.Creeper
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.util.Vector
import kotlin.math.pow

class ArrowBombCreeper(val count: Int, val damage: Double): EntityComponent {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "count" to count,
            "damage" to damage
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.ARROWBOMB_CREEPER
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            val newCount = map["count"].toInt()
            val newDamage = map["damage"].toDouble()
            return ArrowBombCreeper(newCount, newDamage)
        }
    }

    override fun handle(ctx: EntityEventContext, wrapper: EntityWrapper) {
        when (val e = ctx.event) {

            is EntityExplodeEvent -> {
                if (e.entity.getTag<Boolean>("exploding") != true) return
                for (i in 1..count) {
                    val arrow = e.entity.world.spawnArrow(
                        e.entity.location,
                        Vector(
                            Utils.randomRange(-1.0, 1.0),
                            Math.random(),
                            Utils.randomRange(-1.0, 1.0)
                        ).normalize(),
                        1F,
                        1F
                    )

                    arrow.shooter = e.entity as Creeper
                    EntityWrapperManager.register(arrow.uniqueId, EntityWrapper(arrow,
                        mutableListOf(

                            CustomDamageProjectile(
                                HitEffects(
                                    CustomDamageApply(
                                        damage,
                                        DamageType.ARROW,
                                        0
                                    )
                                )
                            )

                        ))
                    )

                }
            }

        }
    }
}