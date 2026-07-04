package me.newburyminer.customItems.structures

import me.newburyminer.customItems.Utils.Companion.beautify
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobEntry
import me.newburyminer.customItems.mobprovider.MobProvider

interface StructureDefinition {
    val id: String
    val name: String get() = id.beautify()

    val mobProvider: MobProvider

    val normalSpawner: TrialSpawnerDefinition
    val ominousSpawner: TrialSpawnerDefinition

    val normalVault: VaultDefinition
    val ominousVault: VaultDefinition

    operator fun MobDefinition.times(multiplier: Double): MobEntry =
        MobEntry(this, multiplier)
}