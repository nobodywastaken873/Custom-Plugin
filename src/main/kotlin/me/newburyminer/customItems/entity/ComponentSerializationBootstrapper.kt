package me.newburyminer.customItems.entity

import org.bukkit.plugin.java.JavaPlugin
import org.reflections.Reflections
import java.lang.reflect.Modifier

object ComponentSerializationBootstrapper {
    fun registerAll(plugin: JavaPlugin) {
        val reflections: Reflections = Reflections("me.newburyminer.customItems")
        val classes = reflections.getSubTypesOf(EntityComponent::class.java)
            .filter { !Modifier.isAbstract(it.modifiers) && !it.isInterface }

        for (cls in classes) {
            val instance = cls.getDeclaredConstructor().newInstance()
            val componentType = instance.componentType
            ComponentSerializationRegistry.register(componentType, instance)
            //plugin.logger.info("Successfully registered ${customId.realName}")
        }
        plugin.logger.info("Successfully registered all entity components")
    }
}