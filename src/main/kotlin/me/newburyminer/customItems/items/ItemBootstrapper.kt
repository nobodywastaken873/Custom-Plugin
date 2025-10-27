package me.newburyminer.customItems.items

import me.newburyminer.customItems.systems.playertask.PlayerTask
import me.newburyminer.customItems.systems.playertask.PlayerTaskHandler
import org.bukkit.plugin.java.JavaPlugin
import org.reflections.Reflections
import java.lang.reflect.Modifier

object ItemBootstrapper {
    fun registerAll(plugin: JavaPlugin) {
        val reflections: Reflections = Reflections("me.newburyminer.customItems.items")
        val classes = reflections.getSubTypesOf(CustomItemDefinition::class.java)
            .filter { !Modifier.isAbstract(it.modifiers) && !it.isInterface }

        for (cls in classes) {
            val instance = cls.getDeclaredConstructor().newInstance()
            val customId = instance.custom
            val item = instance.item
            ItemRegistry.register(customId, item)
            ItemEventHandler.register(customId, instance)
            instance.extraTasks.forEach { (period, runTask) ->
                PlayerTaskHandler.registerTask(period, PlayerTask { player ->  runTask(player)})}

            //plugin.logger.info("Successfully registered ${customId.realName}")
        }
        plugin.logger.info("Successfully registered all items")
    }
}