package me.newburyminer.customItems.structures.structure

import me.newburyminer.customItems.mobprovider.MobEntry
import me.newburyminer.customItems.mobprovider.MobProvider
import me.newburyminer.customItems.mobprovider.mobs.BasicZombie
import me.newburyminer.customItems.mobprovider.mobs.rocky.AncientDefender
import me.newburyminer.customItems.mobprovider.mobs.rocky.GraniteShell
import me.newburyminer.customItems.mobprovider.mobs.rocky.InfestedGeologist
import me.newburyminer.customItems.mobprovider.mobs.rocky.LeadenSkeleton
import me.newburyminer.customItems.mobprovider.mobs.rocky.RockGolem
import me.newburyminer.customItems.structures.EncounterStyle
import me.newburyminer.customItems.structures.StructureDefinition
import me.newburyminer.customItems.structures.TrialSpawnerDefinition
import me.newburyminer.customItems.structures.VaultDefinition

object BrokenManor : StructureDefinition {

    override val id: String = "broken_manor"

    override val mobProvider: MobProvider = MobProvider(
        EncounterStyle.DEFAULT,

        InfestedGeologist * 1.2,
        LeadenSkeleton,

        AncientDefender * 0.6,
        RockGolem,

        GraniteShell
    )

    override val normalSpawner: TrialSpawnerDefinition = TrialSpawnerDefinition(loot = 1)
    override val ominousSpawner: TrialSpawnerDefinition = TrialSpawnerDefinition(loot = 1)

    override val normalVault: VaultDefinition = VaultDefinition(loot = 1)
    override val ominousVault: VaultDefinition = VaultDefinition(loot = 1)
}