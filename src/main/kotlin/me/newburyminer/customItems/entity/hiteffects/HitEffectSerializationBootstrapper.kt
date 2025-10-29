package me.newburyminer.customItems.entity.hiteffects

import me.newburyminer.customItems.entity.ComponentSerializationRegistry
import me.newburyminer.customItems.entity.EntityComponent
import org.bukkit.plugin.java.JavaPlugin
import org.reflections.Reflections
import java.lang.reflect.Modifier

object HitEffectSerializationBootstrapper {
    fun registerAll(plugin: JavaPlugin) {
        val reflections: Reflections = Reflections("me.newburyminer.customItems")
        val classes = reflections.getSubTypesOf(HitEffect::class.java)
            .filter { !Modifier.isAbstract(it.modifiers) && !it.isInterface }

        for (cls in classes) {
            val instance = cls.getDeclaredConstructor().newInstance()
            val componentType = instance.hitEffectType
            HitEffectSerializationRegistry.register(componentType, instance)
            //plugin.logger.info("Successfully registered ${customId.realName}")
        }
        plugin.logger.info("Successfully registered all hit effects")
    }
}