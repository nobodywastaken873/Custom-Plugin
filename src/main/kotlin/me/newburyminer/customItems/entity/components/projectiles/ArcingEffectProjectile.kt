package me.newburyminer.customItems.entity.components.projectiles

import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent
import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.components.projectileshooters.ProjectileDamageShooter
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.helpers.CustomDamageType.Companion.isCustom
import me.newburyminer.customItems.helpers.CustomEffects
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Projectile
import org.bukkit.event.entity.EntityChangeBlockEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import java.util.UUID

class ArcingEffectProjectile(
    private val damage: HitEffects,
    private val caster: Entity?,
): EntityComponent {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "damage" to damage.serialize(),
            "caster" to caster?.uniqueId.toString()
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.ARCING_EFFECT_PROJECTILE
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return ArcingEffectProjectile(
                HitEffects.deserialize(map["damage"]),
                Bukkit.getEntity(UUID.fromString(map["caster"].asString()))
            )
        }
    }

    override fun registerListeners(wrapper: EntityWrapper) {
        register(EntityChangeBlockEvent::class, wrapper.entity.uniqueId, { e ->
            e.entity == wrapper.entity
        },
        {e ->

            e.isCancelled = true
            val loc = e.entity.location
            damage.applyTargetless(caster ?: return@register, loc)
            wrapper.entity.remove()

            CustomEffects.playSound(loc, Sound.ENTITY_GENERIC_BIG_FALL, 1f, 1.4F)

        })
    }
}