package me.newburyminer.customItems.structures.structure

import me.newburyminer.customItems.loot.StructureLoot
import me.newburyminer.customItems.loot.providers.structure.AbandonedShipLoot
import me.newburyminer.customItems.mobprovider.MobProvider
import me.newburyminer.customItems.mobprovider.mobs.desert.CursedArcher
import me.newburyminer.customItems.mobprovider.mobs.desert.MaskedMummy
import me.newburyminer.customItems.mobprovider.mobs.desert.PinbagCreeper
import me.newburyminer.customItems.mobprovider.mobs.desert.RobedArcher
import me.newburyminer.customItems.mobprovider.mobs.desert.SandTurret
import me.newburyminer.customItems.mobprovider.mobs.desert.SandstormMage
import me.newburyminer.customItems.mobprovider.mobs.desert.TempleGolem
import me.newburyminer.customItems.structures.EncounterStyle
import me.newburyminer.customItems.structures.StructureDefinition

object DesertTemple : StructureDefinition {

    override val id: String = "desert_temple"

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

    override val lootProvider: StructureLoot =
        AbandonedShipLoot

}