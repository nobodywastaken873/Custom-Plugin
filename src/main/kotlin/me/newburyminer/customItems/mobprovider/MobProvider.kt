package me.newburyminer.customItems.mobprovider

import kotlin.math.ln
import kotlin.random.Random

class MobProvider(
    val tierChances: MobTierMap,
    vararg entry: SpawnOption
) {

    val entries: List<MobEntry> = entry.map {
        when (it) {
            is MobDefinition -> MobEntry(it)
            is MobEntry -> it
        }
    }

    fun new(context: MobContext): List<MobDefinition> {
       val pickedTier = tierChances.getRandomTier(context)
       return entries
            .filter { (definition, _) ->
                definition.tier == pickedTier
            }
            .sortedBy { (_, multiplier) ->
                -ln(Random.nextDouble()) / multiplier
            }
            .map { (definition, _) ->
                definition
            }
    }
}