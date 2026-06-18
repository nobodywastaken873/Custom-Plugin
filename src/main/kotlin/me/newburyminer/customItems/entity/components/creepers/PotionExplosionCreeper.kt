package me.newburyminer.customItems.entity.components.creepers

import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import org.bukkit.NamespacedKey
import org.bukkit.entity.AreaEffectCloud
import org.bukkit.entity.Creeper
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class PotionExplosionCreeper(
    val type: PotionEffectType,
    val duration: Int,
    val potency: Int,
    val ambient: Boolean = false,
    val showParticles: Boolean = true
): EntityComponent {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "type" to type.key.asString(),
            "duration" to duration,
            "potency" to potency,
            "ambient" to ambient,
            "showparticles" to showParticles
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.POTION_EXPLOSION_CREEPER
        override fun deserialize(map: Map<String, Any>): PotionExplosionCreeper {
            val key = NamespacedKey.fromString(map["type"].asString())!!
            val newType = RegistryAccess.registryAccess().getRegistry(RegistryKey.MOB_EFFECT).get(key)!!
            val newDuration = map["duration"].asInt()
            val newPotency = map["potency"].asInt()
            val newAmbient = map["ambient"].asBoolean()
            val newShowParticles = map["showparticles"].asBoolean()
            return PotionExplosionCreeper(newType, newDuration, newPotency, newAmbient, newShowParticles)
        }
    }

    override fun registerListeners(wrapper: EntityWrapper) {
        register(EntityExplodeEvent::class, wrapper.entity.uniqueId, { e ->
            e.entity == wrapper.entity
        },
        {e ->
            val creeper = e.entity as? Creeper ?: return@register
            creeper.location.world.spawn(
                creeper.location,
                AreaEffectCloud::class.java
            ) {
                it.duration = duration
                it.durationOnUse = 0
                it.customEffects.add(PotionEffect(
                    type,
                    duration,
                    potency,
                    ambient,
                    showParticles
                ))
            }
        })
    }

    /*override fun handle(ctx: EntityEventContext, wrapper: EntityWrapper) {
        when (val e = ctx.event) {

            is EntityExplodeEvent -> {
                if (e.entity.getTag<Boolean>("exploding") != true) return
                val creeper = e.entity as? Creeper ?: return
                creeper.addPotionEffect(PotionEffect(
                    type,
                    duration,
                    potency,
                    ambient,
                    showParticles
                ))
            }

        }
    }*/

}