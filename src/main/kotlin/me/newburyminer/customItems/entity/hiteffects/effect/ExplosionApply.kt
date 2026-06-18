package me.newburyminer.customItems.entity.hiteffects.effect

import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.projectiles.SimpleEffectAura
import me.newburyminer.customItems.entity.hiteffects.HitEffect
import me.newburyminer.customItems.entity.hiteffects.HitEffectDeserialization
import me.newburyminer.customItems.entity.hiteffects.HitEffectType
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.helpers.ParticleTheme
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Marker
import org.bukkit.entity.Player
import org.bukkit.util.Vector

class ExplosionApply(
    private val power: Float,
    private val setFire: Boolean,
    private val breakBlocks: Boolean = false
): HitEffect {

    override fun apply(victim: LivingEntity, damager: Entity, sourceLoc: Location?) {
        damager.world.createExplosion(damager, sourceLoc ?: damager.location, power, setFire, breakBlocks)
    }

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "power" to power,
            "setFire" to setFire,
            "breakBlocks" to breakBlocks
        )
    }
    companion object: HitEffectDeserialization {
        override val componentType: HitEffectType = HitEffectType.EXPLOSION
        override fun deserialize(map: Map<String, Any>): HitEffect {
            return ExplosionApply(
                map["power"].asFloat(),
                map["setFire"].asBoolean(),
                map["breakBlocks"].asBoolean(),
            )
        }
    }
}