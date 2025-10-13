package me.newburyminer.customItems.items.armorsets

import me.newburyminer.customItems.Utils.Companion.readableName
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.ItemEventHandler
import me.newburyminer.customItems.items.ItemRegistry
import org.bukkit.plugin.java.JavaPlugin
import org.reflections.Reflections

object ArmorSetBootstrapper {
    fun registerAll(plugin: JavaPlugin) {
        val reflections: Reflections = Reflections("me.newburyminer.customItems.items")
        val classes = reflections.getSubTypesOf(ArmorSetBehavior::class.java)

        for (cls in classes) {
            val instance = cls.getDeclaredConstructor().newInstance()
            val armorSet = instance.set
            ArmorSetEventHandler.register(armorSet, instance)
            plugin.logger.info("Successfully registered ${armorSet.readableName()} set")
        }
        plugin.logger.info("Successfully registered all armor sets")
    }
}