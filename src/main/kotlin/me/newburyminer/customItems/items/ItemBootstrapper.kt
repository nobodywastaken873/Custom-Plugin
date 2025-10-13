package me.newburyminer.customItems.items

import org.bukkit.plugin.java.JavaPlugin
import org.reflections.Reflections

object ItemBootstrapper {
    fun registerAll(plugin: JavaPlugin) {
        val reflections: Reflections = Reflections("me.newburyminer.customItems.items")
        val classes = reflections.getSubTypesOf(CustomItemDefinition::class.java)

        for (cls in classes) {
            val instance = cls.getDeclaredConstructor().newInstance()
            val customId = instance.custom
            val item = instance.item
            ItemRegistry.register(customId, item)
            ItemEventHandler.register(customId, instance)
            plugin.logger.info("Successfully registered ${customId.realName}")
        }
        plugin.logger.info("Successfully registered all items")
    }
}