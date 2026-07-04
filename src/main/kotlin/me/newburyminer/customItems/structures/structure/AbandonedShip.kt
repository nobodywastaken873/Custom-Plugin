package me.newburyminer.customItems.structures.structure

import me.newburyminer.customItems.mobprovider.MobEntry
import me.newburyminer.customItems.mobprovider.MobProvider
import me.newburyminer.customItems.mobprovider.MobTierMap
import me.newburyminer.customItems.mobprovider.mobs.BasicZombie
import me.newburyminer.customItems.mobprovider.mobs.coldocean.BrutishSailor
import me.newburyminer.customItems.mobprovider.mobs.coldocean.Buccaneer
import me.newburyminer.customItems.mobprovider.mobs.coldocean.ChainmailFighter
import me.newburyminer.customItems.mobprovider.mobs.coldocean.FattenedMaggot
import me.newburyminer.customItems.mobprovider.mobs.coldocean.PirateCrew
import me.newburyminer.customItems.mobprovider.mobs.coldocean.PirateGunner
import me.newburyminer.customItems.mobprovider.mobs.coldocean.ProjectileVomitingPirate
import me.newburyminer.customItems.mobprovider.mobs.coldocean.SickenedPirate
import me.newburyminer.customItems.structures.EncounterStyle
import me.newburyminer.customItems.structures.StructureDefinition
import me.newburyminer.customItems.structures.TrialSpawnerDefinition
import me.newburyminer.customItems.structures.VaultDefinition

object AbandonedShip : StructureDefinition {

    override val id: String = "abandoned_ship"

    override val mobProvider: MobProvider = MobProvider(
        EncounterStyle.DEFAULT,

        Buccaneer * 0.8,
        PirateCrew * 1.5,
        FattenedMaggot * 1.2,

        PirateGunner * 0.8,
        ProjectileVomitingPirate * 1.2,
        SickenedPirate,
        BrutishSailor * 1.1,

        ChainmailFighter
    )

    override val normalSpawner: TrialSpawnerDefinition = TrialSpawnerDefinition(loot = 1)
    override val ominousSpawner: TrialSpawnerDefinition = TrialSpawnerDefinition(loot = 1)

    override val normalVault: VaultDefinition = VaultDefinition(loot = 1)
    override val ominousVault: VaultDefinition = VaultDefinition(loot = 1)
}