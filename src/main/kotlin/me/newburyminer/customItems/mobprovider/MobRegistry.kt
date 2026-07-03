package me.newburyminer.customItems.mobprovider

import me.newburyminer.customItems.structures.StructureDefinition
import me.newburyminer.customItems.structures.StructureRegistry
import org.bukkit.plugin.Plugin
import org.reflections.Reflections
import java.lang.reflect.Modifier

object MobRegistry {

    val registry: MutableMap<String, MobDefinition> = mutableMapOf()

    private fun register(mob: MobDefinition) {
        registry[mob.id] = mob
    }

    fun bootstrap(plugin: Plugin) {
        val reflections: Reflections = Reflections("me.newburyminer.customItems.mobprovider")
        val classes = reflections.getSubTypesOf(MobDefinition::class.java)
            .filter { !Modifier.isAbstract(it.modifiers) && !it.isInterface }

        for (cls in classes) {
            val kClass = cls.kotlin
            val mobObject = kClass.objectInstance ?: continue

            register(mobObject)
        }
        plugin.logger.info("Successfully registered all mob definitions")
    }

    fun getMob(id: String): MobDefinition? {
        return registry[id]
    }

    fun getEntries(): List<String> {
        return registry.keys.toList()
    }

}