package me.newburyminer.customItems.entity.components.projectiles

import me.newburyminer.customItems.effects.CustomEffectType
import me.newburyminer.customItems.effects.EffectData
import me.newburyminer.customItems.effects.EffectManager
import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityEventContext
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.helpers.CustomEffects
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import java.util.UUID

class ElytraBreakerFirework(private val damage: HitEffects, private val duration: Int, private val target: Player):
    EntityComponent {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "damage" to damage.serialize(),
            "target" to target.uniqueId,
            "duration" to duration,
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.ELYTRA_BREAKER_FIREWORK
        override fun deserialize(map: Map<String, Any>): EntityComponent? {
            val newDamage = HitEffects.deserialize(map["damage"])
            val newUUID = (map["target"] ?: return null) as UUID
            val newTarget = Bukkit.getPlayer(newUUID) ?: return null
            val newDuration = map["duration"].toInt()
            return ElytraBreakerFirework(newDamage, newDuration, newTarget)
        }
    }

    override fun tick(wrapper: EntityWrapper) {

        if (!target.isValid) {
            (wrapper.entity as Firework).detonate()
            return
        }
        if (wrapper.entity.location.distanceSquared(target.location) < 10) {
            val firework = wrapper.entity as Firework
            firework.detonate()
            return
        }

        val newDirection = target.location.subtract(wrapper.entity.location).toVector().normalize()
        wrapper.entity.velocity = newDirection.multiply(4.5)

    }

    override fun registerListeners(wrapper: EntityWrapper) {
        register(EntityDamageByEntityEvent::class, wrapper.entity.uniqueId, { e ->
            e.damager == wrapper.entity
        },
        {e ->
            val player = e.entity as? Player ?: return@register
            player.isGliding = false
            EffectManager.applyEffect(player, CustomEffectType.ELYTRA_DISABLED, EffectData(duration, unique = true))
            player.setCooldown(Material.ELYTRA, 500)
            CustomEffects.playSound(e.damager.location, Sound.ENTITY_SHEEP_SHEAR, 1.0F, 0.8F)

            e.isCancelled = true
            damage.apply(player, e.damager)
        })
    }

    /*override fun handle(ctx: EntityEventContext, wrapper: EntityWrapper) {
        when (val e = ctx.event) {

            is EntityDamageByEntityEvent -> {
                if (e.damager != wrapper.entity) return
                val player = e.entity as? Player ?: return
                player.isGliding = false
                EffectManager.applyEffect(player, CustomEffectType.ELYTRA_DISABLED, EffectData(duration, unique = true))
                player.setCooldown(Material.ELYTRA, 500)
                CustomEffects.Companion.playSound(e.damager.location, Sound.ENTITY_SHEEP_SHEAR, 1.0F, 0.8F)

                e.isCancelled = true
                damage.apply(player, e.damager)
            }

        }
    }*/

}