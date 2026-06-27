package me.newburyminer.customItems.structures

import me.newburyminer.customItems.Utils.Companion.beautify

interface StructureDefinition {
    val id: String
    val name: String get() = id.beautify()

    val normalSpawner: TrialSpawnerDefinition
    val ominousSpawner: TrialSpawnerDefinition

    val normalVault: VaultDefinition
    val ominousVault: VaultDefinition
}