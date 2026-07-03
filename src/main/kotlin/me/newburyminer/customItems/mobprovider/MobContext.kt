package me.newburyminer.customItems.mobprovider

import me.newburyminer.customItems.structures.StructureDefinition
import me.newburyminer.customItems.structures.StructureReference
import org.bukkit.Location

class MobContext(
    distance: Double,
    val spawnerType: StructureReference.Difficulty,
    val structure: StructureDefinition,
    val location: Location,
) {
    val difficulty: Double =
        distance / 600.0 *
                when (spawnerType) {
                    StructureReference.Difficulty.NORMAL -> 1.0
                    StructureReference.Difficulty.OMINOUS -> 1.5
                }

    //  TODO: also factor fear or wtv into calcs
}