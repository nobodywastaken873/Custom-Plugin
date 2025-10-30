package me.newburyminer.customItems.entity.hiteffects

import org.bukkit.plugin.Plugin
import org.reflections.Reflections
import java.lang.reflect.Modifier

object HitEffectSerializationRegistry {

    private val registry = mutableMapOf<HitEffectType, HitEffect>()

    fun register(type: HitEffectType, component: HitEffect) {
        registry[type] = component
    }

    fun deserialize(type: HitEffectType, map: Map<String, Any>): HitEffect? {

        val deserializer = registry[type] ?: return null
        return deserializer.deserialize(map)

    }

    fun bootstrap(plugin: Plugin) {
        val reflections: Reflections = Reflections("me.newburyminer.customItems")
        val classes = reflections.getSubTypesOf(HitEffect::class.java)
            .filter { !Modifier.isAbstract(it.modifiers) && !it.isInterface }

        for (cls in classes) {
            val instance = cls.getDeclaredConstructor().newInstance()
            val componentType = instance.hitEffectType
            register(componentType, instance)
            //plugin.logger.info("Successfully registered ${customId.realName}")
        }
        plugin.logger.info("Successfully registered all hit effects")
    }

}