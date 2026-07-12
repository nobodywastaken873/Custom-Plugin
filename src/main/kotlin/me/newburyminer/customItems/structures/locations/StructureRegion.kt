package me.newburyminer.customItems.structures.locations

import me.newburyminer.customItems.structures.StructureDefinition
import org.bukkit.util.BoundingBox

data class StructureRegion(
    val definition: StructureDefinition,
    val bounds: BoundingBox
)
