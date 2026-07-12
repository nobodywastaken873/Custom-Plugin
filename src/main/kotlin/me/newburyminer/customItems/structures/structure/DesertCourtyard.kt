package me.newburyminer.customItems.structures.structure

import me.newburyminer.customItems.loot.StructureLoot
import me.newburyminer.customItems.loot.providers.structure.AbandonedShip
import me.newburyminer.customItems.mobprovider.MobProvider
import me.newburyminer.customItems.mobprovider.mobs.desert.AntlionSpirit
import me.newburyminer.customItems.mobprovider.mobs.desert.CastingCorpse
import me.newburyminer.customItems.mobprovider.mobs.desert.CursedArcher
import me.newburyminer.customItems.mobprovider.mobs.desert.LeapingSpider
import me.newburyminer.customItems.mobprovider.mobs.desert.RobedArcher
import me.newburyminer.customItems.mobprovider.mobs.desert.RobedSkirmisher
import me.newburyminer.customItems.mobprovider.mobs.desert.SandstormMage
import me.newburyminer.customItems.mobprovider.mobs.desert.SandySniper
import me.newburyminer.customItems.mobprovider.mobs.desert.ScarabBeetleBomber
import me.newburyminer.customItems.mobprovider.mobs.desert.ScarabSoldier
import me.newburyminer.customItems.structures.EncounterStyle
import me.newburyminer.customItems.structures.StructureDefinition

object DesertCourtyard : StructureDefinition {

    override val id: String = "desert_courtyard"

    override val mobProvider: MobProvider = MobProvider(
        EncounterStyle.ELITE.modifyMiniboss(2),

        RobedArcher * 1.4,
        ScarabBeetleBomber * 1.1,
        ScarabSoldier,

        SandySniper,
        LeapingSpider * 1.2,
        CursedArcher * 1.4,
        CastingCorpse,

        AntlionSpirit * 1.3,
        SandstormMage,

        RobedSkirmisher
    )

    override val lootProvider: StructureLoot =
        AbandonedShip

}