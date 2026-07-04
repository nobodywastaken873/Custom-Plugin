package me.newburyminer.customItems.structures.structure

import me.newburyminer.customItems.mobprovider.MobEntry
import me.newburyminer.customItems.mobprovider.MobProvider
import me.newburyminer.customItems.mobprovider.mobs.BasicZombie
import me.newburyminer.customItems.mobprovider.mobs.coldocean.Buccaneer
import me.newburyminer.customItems.mobprovider.mobs.coldocean.CaptainsGhost
import me.newburyminer.customItems.mobprovider.mobs.coldocean.ExplosivesMaster
import me.newburyminer.customItems.mobprovider.mobs.coldocean.FattenedMaggot
import me.newburyminer.customItems.mobprovider.mobs.coldocean.GrapplingMaster
import me.newburyminer.customItems.mobprovider.mobs.coldocean.PirateCaptain
import me.newburyminer.customItems.mobprovider.mobs.coldocean.PirateCrew
import me.newburyminer.customItems.mobprovider.mobs.coldocean.PirateMachineGunner
import me.newburyminer.customItems.mobprovider.mobs.coldocean.ProjectileVomitingPirate
import me.newburyminer.customItems.mobprovider.mobs.coldocean.RopeswingerCrew
import me.newburyminer.customItems.mobprovider.mobs.coldocean.SickenedPirate
import me.newburyminer.customItems.structures.EncounterStyle
import me.newburyminer.customItems.structures.StructureDefinition
import me.newburyminer.customItems.structures.TrialSpawnerDefinition
import me.newburyminer.customItems.structures.VaultDefinition

object WreckedGalleon : StructureDefinition {

    override val id: String = "wrecked_galleon"

    override val mobProvider: MobProvider = MobProvider(
        EncounterStyle.ELITE,

        PirateCrew,
        Buccaneer * 1.3,
        FattenedMaggot,

        RopeswingerCrew,
        ProjectileVomitingPirate,
        SickenedPirate * 1.3,
        GrapplingMaster * 0.8,

        CaptainsGhost,
        ExplosivesMaster * 1.2,

        PirateCaptain
    )

    override val normalSpawner: TrialSpawnerDefinition = TrialSpawnerDefinition(loot = 1)
    override val ominousSpawner: TrialSpawnerDefinition = TrialSpawnerDefinition(loot = 1)

    override val normalVault: VaultDefinition = VaultDefinition(loot = 1)
    override val ominousVault: VaultDefinition = VaultDefinition(loot = 1)
}