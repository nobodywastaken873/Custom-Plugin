package me.newburyminer.customItems.structures

import org.bukkit.plugin.Plugin
import org.reflections.Reflections
import java.lang.reflect.Modifier

object StructureRegistry {

    private val structures = mutableMapOf<String, StructureDefinition>()

    fun lookupLootTag(tag: String): StructureReference {
        //val wholeTag = e.entity.itemStack.itemMeta.customModelDataComponent.strings.first()

        val splitIndex =
            if (tag.indexOf("normal") != -1) tag.indexOf("normal")
            else tag.indexOf("ominous")
        val structure = structures[tag.substring(0, splitIndex - 1)] ?: TODO()

        val secondHalf = tag.substring(splitIndex)
        val difficulty = when (secondHalf.split("_")[0]) {
            "normal" -> StructureReference.Difficulty.NORMAL
            "ominous" -> StructureReference.Difficulty.OMINOUS
            else -> StructureReference.Difficulty.NORMAL
        }
        val type = when (secondHalf.split("_")[1]) {
            "spawner" -> StructureReference.Type.SPAWNER
            "vault" -> StructureReference.Type.VAULT
            else -> StructureReference.Type.SPAWNER
        }

        return StructureReference(
            structure,
            difficulty,
            type,
        )
    }

    fun bootstrap(plugin: Plugin) {
        val reflections: Reflections = Reflections("me.newburyminer.customItems.structures")
        val classes = reflections.getSubTypesOf(StructureDefinition::class.java)
            .filter { !Modifier.isAbstract(it.modifiers) && !it.isInterface }

        for (cls in classes) {
            val kClass = cls.kotlin
            val structureObject = kClass.objectInstance ?: continue

            register(structureObject)
        }
        plugin.logger.info("Successfully registered all structures")
    }

    fun register(structure: StructureDefinition) {
        structures[structure.id] = structure
    }

}