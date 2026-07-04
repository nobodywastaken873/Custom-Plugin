package me.newburyminer.customItems.structures.structure

import me.newburyminer.customItems.mobprovider.MobEntry
import me.newburyminer.customItems.mobprovider.MobProvider
import me.newburyminer.customItems.mobprovider.mobs.BasicZombie
import me.newburyminer.customItems.mobprovider.mobs.desert.CursedArcher
import me.newburyminer.customItems.mobprovider.mobs.desert.MaskedMummy
import me.newburyminer.customItems.mobprovider.mobs.desert.PinbagCreeper
import me.newburyminer.customItems.mobprovider.mobs.desert.RobedArcher
import me.newburyminer.customItems.mobprovider.mobs.desert.SandTurret
import me.newburyminer.customItems.mobprovider.mobs.desert.SandstormMage
import me.newburyminer.customItems.mobprovider.mobs.desert.TempleGolem
import me.newburyminer.customItems.structures.EncounterStyle
import me.newburyminer.customItems.structures.StructureDefinition
import me.newburyminer.customItems.structures.TrialSpawnerDefinition
import me.newburyminer.customItems.structures.VaultDefinition

object BadlandsPyramid : StructureDefinition {

    override val id: String = "badlands_pyramid"

    override val mobProvider: MobProvider = MobProvider(
        EncounterStyle.SWARM.modifyElite(4),

        MaskedMummy * 1.2,
        PinbagCreeper,
        RobedArcher,

        TempleGolem * 1.4,
        CursedArcher,
        SandTurret,

        SandstormMage
    )

    override val normalSpawner: TrialSpawnerDefinition = TrialSpawnerDefinition(loot = 1)
    override val ominousSpawner: TrialSpawnerDefinition = TrialSpawnerDefinition(loot = 1)

    override val normalVault: VaultDefinition = VaultDefinition(loot = 1)
    override val ominousVault: VaultDefinition = VaultDefinition(loot = 1)
}