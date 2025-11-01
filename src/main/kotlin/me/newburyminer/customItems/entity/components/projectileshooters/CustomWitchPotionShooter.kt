package me.newburyminer.customItems.entity.components.projectileshooters

import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityEventContext
import me.newburyminer.customItems.entity.EntityWrapper
import org.bukkit.Color
import org.bukkit.NamespacedKey
import org.bukkit.entity.Entity
import org.bukkit.entity.SplashPotion
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.potion.PotionEffect

class CustomWitchPotionShooter(private val effects: List<PotionEffect>): EntityComponent {
    override val componentType: EntityComponentType = EntityComponentType.CUSTOM_WITCH_POTION

    override fun serialize(): Map<String, Any> {
        return effects.associate {
            it.type.key.asString() to mapOf(
                "duration" to it.duration,
                "amplifier" to it.amplifier,
            )
        }
    }
    @Suppress("UNCHECKED_CAST")
    override fun deserialize(map: Map<String, Any>): EntityComponent? {
        val effects = map.entries.map {
            val key = NamespacedKey.fromString(it.key) ?: return null
            val type = RegistryAccess
                .registryAccess()
                .getRegistry(RegistryKey.MOB_EFFECT)
                .get(key) ?: return null
            val innerMap = it.value as Map<String, Any>
            val duration = innerMap["duration"] as Int
            val amplifier = innerMap["amplifier"] as Int
            PotionEffect(type, duration, amplifier)
        }
        return CustomWitchPotionShooter(effects)
    }

    override fun handle(ctx: EntityEventContext, wrapper: EntityWrapper) {
        when (val e = ctx.event) {

            is ProjectileLaunchEvent -> {
                val effect = effects.random()
                val newEffect = PotionEffect(effect.type, effect.duration, effect.amplifier)
                val potion = e.entity as SplashPotion
                val newMeta = potion.potionMeta
                newMeta.basePotionType = null
                newMeta.clearCustomEffects()
                newMeta.addCustomEffect(newEffect, true)
                newMeta.color = Color.fromRGB((Math.random() * 255).toInt(), (Math.random() * 255).toInt(), (Math.random() * 255).toInt())
                potion.potionMeta = newMeta
            }

        }
    }
}