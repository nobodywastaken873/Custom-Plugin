package me.newburyminer.customItems.entity.components.creepers

import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityEventContext
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.hiteffects.HitEffect
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaEffectApply
import org.bukkit.NamespacedKey
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

    override val componentType: EntityComponentType = EntityComponentType.POTION_EXPLOSION_CREEPER

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "type" to type.key.asString(),
            "duration" to duration,
            "potency" to potency,
            "ambient" to ambient,
            "showparticles" to showParticles
        )
    }
    override fun deserialize(map: Map<String, Any>): PotionExplosionCreeper {
        val key = NamespacedKey.fromString(map["type"] as String)!!
        val newType = RegistryAccess.registryAccess().getRegistry(RegistryKey.MOB_EFFECT).get(key)!!
        val newDuration = map["duration"] as Int
        val newPotency = map["potency"] as Int
        val newAmbient = map["ambient"] as Boolean
        val newShowParticles = map["showparticles"] as Boolean
        return PotionExplosionCreeper(newType, newDuration, newPotency, newAmbient, newShowParticles)
    }

    override fun handle(ctx: EntityEventContext, wrapper: EntityWrapper) {
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
    }

}