package me.newburyminer.customItems.loot

import me.newburyminer.customItems.mobprovider.MobDefinition
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import org.reflections.Reflections
import java.lang.reflect.Modifier

object LootRegistry {

    val registry: MutableMap<String, LootProvider> = mutableMapOf()

    private fun register(provider: LootProvider) {
        registry[provider.id] = provider
    }

    fun bootstrap(plugin: Plugin) {
        val reflections: Reflections = Reflections("me.newburyminer.customItems.loot")
        val classes = reflections.getSubTypesOf(LootProvider::class.java)
            .filter { !Modifier.isAbstract(it.modifiers) && !it.isInterface }

        for (cls in classes) {
            val kClass = cls.kotlin
            val mobObject = kClass.objectInstance ?: continue

            register(mobObject)
        }
        plugin.logger.info("Successfully registered all loot providers")
    }

    fun getProvider(id: String): LootProvider {
        return registry[id] ?: TODO("empty provider")
    }

    fun getEntries(): List<String> {
        return registry.keys.toList()
    }

}