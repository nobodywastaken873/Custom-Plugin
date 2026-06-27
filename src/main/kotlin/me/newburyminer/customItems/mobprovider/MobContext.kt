package me.newburyminer.customItems.mobprovider

import me.newburyminer.customItems.structures.StructureDefinition
import me.newburyminer.customItems.structures.StructureReference
import org.bukkit.Location

data class MobContext(
    val difficulty: Double,
    val spawnerType: StructureReference.Difficulty,
    val structure: StructureDefinition,
    val location: Location,
)