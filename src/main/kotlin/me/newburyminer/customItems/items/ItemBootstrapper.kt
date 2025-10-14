package me.newburyminer.customItems.items

import me.newburyminer.customItems.systems.playertask.PlayerTask
import me.newburyminer.customItems.systems.playertask.PlayerTaskHandler
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
            if (cls.declaredMethods.any { it.name == "runTask" })
                PlayerTaskHandler.registerTask(instance.period, PlayerTask { player ->  instance.runTask(player)})

            plugin.logger.info("Successfully registered ${customId.realName}")
        }
        plugin.logger.info("Successfully registered all items")
    }
}