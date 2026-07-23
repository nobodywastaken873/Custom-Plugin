package me.newburyminer.customItems.structures.structure

import me.newburyminer.customItems.loot.StructureLoot
import me.newburyminer.customItems.loot.providers.structure.AbandonedShipLoot
import me.newburyminer.customItems.mobprovider.MobProvider
import me.newburyminer.customItems.mobprovider.mobs.desert.*
import me.newburyminer.customItems.structures.EncounterStyle
import me.newburyminer.customItems.structures.StructureDefinition

object DesertMosque : StructureDefinition {

    override val id: String = "desert_mosque"

    override val mobProvider: MobProvider = MobProvider(
        EncounterStyle.STANDARD,

        ScarabBeetleBomber,
        ScarabSoldier,
        RobedArcher * 1.3,

        CastingCorpse,
        LeapingSpider * 1.2,
        SandTurret,
        SandySniper * 0.6,

        AncientBeast * 1.5,
        AntlionSpirit
    )

    override val lootProvider: StructureLoot =
        AbandonedShipLoot

}