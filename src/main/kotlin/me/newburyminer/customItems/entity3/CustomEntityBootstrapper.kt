package me.newburyminer.customItems.entity3

import org.bukkit.plugin.java.JavaPlugin
import org.reflections.Reflections
import java.lang.reflect.Modifier

object CustomEntityBootstrapper {
    fun registerAll(plugin: JavaPlugin) {
        val reflections: Reflections = Reflections("me.newburyminer.customItems.entity")
        val classes = reflections.getSubTypesOf(CustomEntityDefinition::class.java)
            .filter { !Modifier.isAbstract(it.modifiers) && !it.isInterface }

        for (cls in classes) {
            val instance = cls.getDeclaredConstructor().newInstance()
            val entityId = instance.customEntity
            EntitySpawnManager.register(entityId, instance)
            //EntityEventHandler.register(entityId, instance)
            instance.tasks.forEach { (period, runTask) ->
                EntityTaskHandler.registerTask(period, entityId to EntityTask { entity ->  runTask(entity)})}

            //plugin.logger.info("Successfully registered ${customId.realName}")
        }
        plugin.logger.info("Successfully registered all entities")
    }
}