package me.newburyminer.customItems.mobprovider

data class MobEntry(
    val definition: MobDefinition,
    val weight: (MobContext) -> Double
)