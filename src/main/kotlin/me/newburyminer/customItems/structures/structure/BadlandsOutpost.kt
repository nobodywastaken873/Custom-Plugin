package me.newburyminer.customItems.structures.structure

import me.newburyminer.customItems.loot.StructureLoot
import me.newburyminer.customItems.loot.providers.structure.AbandonedShipLoot
import me.newburyminer.customItems.mobprovider.MobProvider
import me.newburyminer.customItems.mobprovider.mobs.desert.AncientBeast
import me.newburyminer.customItems.mobprovider.mobs.desert.AntlionSpirit
import me.newburyminer.customItems.mobprovider.mobs.desert.CastingCorpse
import me.newburyminer.customItems.mobprovider.mobs.desert.LeapingSpider
import me.newburyminer.customItems.mobprovider.mobs.desert.RobedArcher
import me.newburyminer.customItems.mobprovider.mobs.desert.SandTurret
import me.newburyminer.customItems.mobprovider.mobs.desert.SandySniper
import me.newburyminer.customItems.mobprovider.mobs.desert.ScarabBeetleBomber
import me.newburyminer.customItems.mobprovider.mobs.desert.ScarabSoldier
import me.newburyminer.customItems.structures.EncounterStyle
import me.newburyminer.customItems.structures.StructureDefinition

object BadlandsOutpost : StructureDefinition {

    override val id: String = "badlands_outpost"

    override val mobProvider: MobProvider = MobProvider(
        EncounterStyle.STANDARD,

        ScarabBeetleBomber * 0.8,
        ScarabSoldier * 1.3,
        RobedArcher,

        CastingCorpse * 1.2,
        LeapingSpider,
        SandTurret,
        SandySniper * 0.7,

        AncientBeast * 2.0,
        AntlionSpirit
    )

    override val lootProvider: StructureLoot =
        AbandonedShipLoot

}