package me.newburyminer.customItems.structures.structure

import me.newburyminer.customItems.loot.StructureLoot
import me.newburyminer.customItems.loot.providers.structure.AbandonedShip
import me.newburyminer.customItems.mobprovider.MobProvider
import me.newburyminer.customItems.mobprovider.mobs.coldocean.BrutishSailor
import me.newburyminer.customItems.mobprovider.mobs.coldocean.Buccaneer
import me.newburyminer.customItems.mobprovider.mobs.coldocean.CaptainsGhost
import me.newburyminer.customItems.mobprovider.mobs.coldocean.ChainmailFighter
import me.newburyminer.customItems.mobprovider.mobs.coldocean.GrapplingMaster
import me.newburyminer.customItems.mobprovider.mobs.coldocean.HoppingExplosives
import me.newburyminer.customItems.mobprovider.mobs.coldocean.PirateCrew
import me.newburyminer.customItems.mobprovider.mobs.coldocean.PirateGunner
import me.newburyminer.customItems.mobprovider.mobs.coldocean.RopeswingerCrew
import me.newburyminer.customItems.structures.EncounterStyle
import me.newburyminer.customItems.structures.StructureDefinition

object OceanRuins : StructureDefinition {

    override val id: String = "ocean_ruins"

    override val mobProvider: MobProvider = MobProvider(
        EncounterStyle.STANDARD,

        HoppingExplosives * 1.4,
        Buccaneer,
        PirateCrew * 1.2,

        PirateGunner * 1.4,
        RopeswingerCrew,
        BrutishSailor * 1.3,
        GrapplingMaster * 0.65,

        CaptainsGhost,
        ChainmailFighter * 1.1
    )

    override val lootProvider: StructureLoot =
        AbandonedShip

}