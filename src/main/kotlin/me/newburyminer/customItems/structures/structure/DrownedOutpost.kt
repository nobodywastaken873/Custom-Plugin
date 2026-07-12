package me.newburyminer.customItems.structures.structure

import me.newburyminer.customItems.loot.StructureLoot
import me.newburyminer.customItems.loot.providers.structure.AbandonedShip
import me.newburyminer.customItems.mobprovider.MobProvider
import me.newburyminer.customItems.mobprovider.mobs.coldocean.Buccaneer
import me.newburyminer.customItems.mobprovider.mobs.coldocean.CaptainsGhost
import me.newburyminer.customItems.mobprovider.mobs.coldocean.CrazedPrisoner
import me.newburyminer.customItems.mobprovider.mobs.coldocean.HoppingExplosives
import me.newburyminer.customItems.mobprovider.mobs.coldocean.PirateGunner
import me.newburyminer.customItems.mobprovider.mobs.coldocean.PirateMachineGunner
import me.newburyminer.customItems.mobprovider.mobs.coldocean.ProjectileVomitingPirate
import me.newburyminer.customItems.mobprovider.mobs.coldocean.SickenedPirate
import me.newburyminer.customItems.structures.EncounterStyle
import me.newburyminer.customItems.structures.StructureDefinition

object DrownedOutpost : StructureDefinition {

    override val id: String = "drowned_outpost"

    override val mobProvider: MobProvider = MobProvider(
        EncounterStyle.DEFAULT,

        CrazedPrisoner * 1.6,
        HoppingExplosives,
        Buccaneer * 1.2,

        PirateGunner,
        PirateMachineGunner * 1.2,
        SickenedPirate * 1.2,
        ProjectileVomitingPirate * 0.6,

        CaptainsGhost,
    )

    override val lootProvider: StructureLoot =
        AbandonedShip

}