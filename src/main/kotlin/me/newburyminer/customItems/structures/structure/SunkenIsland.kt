package me.newburyminer.customItems.structures.structure

import me.newburyminer.customItems.mobprovider.MobEntry
import me.newburyminer.customItems.mobprovider.MobProvider
import me.newburyminer.customItems.mobprovider.mobs.BasicZombie
import me.newburyminer.customItems.mobprovider.mobs.coldocean.Buccaneer
import me.newburyminer.customItems.mobprovider.mobs.coldocean.CrazedPrisoner
import me.newburyminer.customItems.mobprovider.mobs.coldocean.ExplosivesMaster
import me.newburyminer.customItems.mobprovider.mobs.coldocean.FattenedMaggot
import me.newburyminer.customItems.mobprovider.mobs.coldocean.GrapplingMaster
import me.newburyminer.customItems.mobprovider.mobs.coldocean.PirateMachineGunner
import me.newburyminer.customItems.mobprovider.mobs.coldocean.ProjectileVomitingPirate
import me.newburyminer.customItems.mobprovider.mobs.coldocean.RopeswingerCrew
import me.newburyminer.customItems.structures.EncounterStyle
import me.newburyminer.customItems.structures.StructureDefinition
import me.newburyminer.customItems.structures.TrialSpawnerDefinition
import me.newburyminer.customItems.structures.VaultDefinition

object SunkenIsland : StructureDefinition {

    override val id: String = "sunken_island"

    override val mobProvider: MobProvider = MobProvider(
        EncounterStyle.STANDARD,

        CrazedPrisoner,
        FattenedMaggot * 1.3,
        Buccaneer,

        GrapplingMaster * 0.8,
        RopeswingerCrew * 1.4,
        ProjectileVomitingPirate * 1.2,
        PirateMachineGunner,

        ExplosivesMaster
    )

    override val normalSpawner: TrialSpawnerDefinition = TrialSpawnerDefinition(loot = 1)
    override val ominousSpawner: TrialSpawnerDefinition = TrialSpawnerDefinition(loot = 1)

    override val normalVault: VaultDefinition = VaultDefinition(loot = 1)
    override val ominousVault: VaultDefinition = VaultDefinition(loot = 1)
}