package me.newburyminer.customItems.structures.structure

import me.newburyminer.customItems.loot.StructureLoot
import me.newburyminer.customItems.loot.providers.structure.AbandonedShip
import me.newburyminer.customItems.mobprovider.MobProvider
import me.newburyminer.customItems.mobprovider.mobs.desert.CastingCorpse
import me.newburyminer.customItems.mobprovider.mobs.desert.CursedArcher
import me.newburyminer.customItems.mobprovider.mobs.desert.HuskyDuelist
import me.newburyminer.customItems.mobprovider.mobs.desert.MaskedMummy
import me.newburyminer.customItems.mobprovider.mobs.desert.PinbagCreeper
import me.newburyminer.customItems.mobprovider.mobs.desert.ScarabSoldier
import me.newburyminer.customItems.mobprovider.mobs.desert.TempleGolem
import me.newburyminer.customItems.structures.EncounterStyle
import me.newburyminer.customItems.structures.StructureDefinition

object MassiveDesertTemple : StructureDefinition {

    override val id: String = "massive_desert_temple"

    override val mobProvider: MobProvider = MobProvider(
        EncounterStyle.DEFAULT.modifyElite(5),

        MaskedMummy,
        ScarabSoldier,
        PinbagCreeper * 0.7,

        TempleGolem,
        CursedArcher,
        CastingCorpse * 1.4,

        HuskyDuelist
    )

    override val lootProvider: StructureLoot =
        AbandonedShip

}