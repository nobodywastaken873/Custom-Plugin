package me.newburyminer.customItems.mobprovider

import me.newburyminer.customItems.structures.StructureReference
import org.bukkit.Location

class MobContext(
    distance: Double,
    val spawnerType: StructureReference.Difficulty,
    val location: Location,
) {

    constructor(distance: Double, isOminous: Boolean, location: Location) : this(
        distance,
        if (isOminous) StructureReference.Difficulty.OMINOUS else StructureReference.Difficulty.NORMAL,
        location
    )

    constructor(targetDifficulty: Double, location: Location) : this(
        targetDifficulty * 600.0, false, location
    )

    val difficulty: Double =
        distance / 600.0 *
                when (spawnerType) {
                    StructureReference.Difficulty.NORMAL -> 1.0
                    StructureReference.Difficulty.OMINOUS -> 1.5
                }

    //  TODO: also factor fear or wtv into calcs

    companion object {
        fun calculateDifficulty(location: Location): Double {
            val distance = location.length()
            return distance / 600.0
        }
    }
}