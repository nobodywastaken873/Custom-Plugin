package me.newburyminer.customItems.structures.structure

import me.newburyminer.customItems.mobprovider.MobEntry
import me.newburyminer.customItems.mobprovider.MobProvider
import me.newburyminer.customItems.mobprovider.mobs.BasicZombie
import me.newburyminer.customItems.structures.StructureDefinition
import me.newburyminer.customItems.structures.TrialSpawnerDefinition
import me.newburyminer.customItems.structures.VaultDefinition

object AbandonedShip : StructureDefinition {

    override val id: String = "abandoned_ship"

    override val normalSpawner: TrialSpawnerDefinition = TrialSpawnerDefinition(
        loot = 1,
        mobs = MobProvider(listOf(MobEntry(BasicZombie, {1.0})))
    )
    override val ominousSpawner: TrialSpawnerDefinition = TrialSpawnerDefinition(
        loot = 1,
        mobs = MobProvider(listOf(MobEntry(BasicZombie, {1.0})))
    )

    override val normalVault: VaultDefinition = VaultDefinition(loot = 1)
    override val ominousVault: VaultDefinition = VaultDefinition(loot = 1)
}