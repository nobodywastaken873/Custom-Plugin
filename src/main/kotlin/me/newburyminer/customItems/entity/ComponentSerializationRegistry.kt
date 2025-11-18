package me.newburyminer.customItems.entity

import org.bukkit.plugin.java.JavaPlugin
import org.reflections.Reflections
import java.lang.reflect.Modifier
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance

object ComponentSerializationRegistry {

    private val registry = mutableMapOf<EntityComponentType, DeserializationInterface>()

    fun register(type: EntityComponentType, component: DeserializationInterface) {
        registry[type] = component
    }

    fun deserialize(type: EntityComponentType, map: Map<String, Any>): EntityComponent? {

        val deserializer = registry[type] ?: return null
        return deserializer.deserialize(map)

    }

    fun bootstrap(plugin: JavaPlugin) {
        val reflections: Reflections = Reflections("me.newburyminer.customItems")
        val classes = reflections.getSubTypesOf(EntityComponent::class.java)
            .filter { !Modifier.isAbstract(it.modifiers) && !it.isInterface }

        for (cls in classes) {
            val kClass = cls.kotlin
            val companionObject = kClass.companionObjectInstance as DeserializationInterface

            val componentType = companionObject.componentType
            register(componentType, companionObject)
            //plugin.logger.info("Successfully registered ${customId.realName}")
        }
        plugin.logger.info("Successfully registered all entity components")
    }

}