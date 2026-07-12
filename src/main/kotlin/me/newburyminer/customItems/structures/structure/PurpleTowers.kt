package me.newburyminer.customItems.structures.structure

import me.newburyminer.customItems.loot.StructureLoot
import me.newburyminer.customItems.loot.providers.structure.AbandonedShip
import me.newburyminer.customItems.mobprovider.MobProvider
import me.newburyminer.customItems.mobprovider.mobs.desert.AncientBeast
import me.newburyminer.customItems.mobprovider.mobs.desert.CastingCorpse
import me.newburyminer.customItems.mobprovider.mobs.desert.CursedArcher
import me.newburyminer.customItems.mobprovider.mobs.desert.HuskyDuelist
import me.newburyminer.customItems.mobprovider.mobs.desert.LeapingSpider
import me.newburyminer.customItems.mobprovider.mobs.desert.RobedArcher
import me.newburyminer.customItems.mobprovider.mobs.desert.RobedSkirmisher
import me.newburyminer.customItems.mobprovider.mobs.desert.SandySniper
import me.newburyminer.customItems.mobprovider.mobs.desert.ScarabBeetleBomber
import me.newburyminer.customItems.mobprovider.mobs.desert.ScarabSoldier
import me.newburyminer.customItems.structures.EncounterStyle
import me.newburyminer.customItems.structures.StructureDefinition

object PurpleTowers : StructureDefinition {

    override val id: String = "purple_towers"

    override val mobProvider: MobProvider = MobProvider(
        EncounterStyle.ELITE,

        RobedArcher,
        ScarabBeetleBomber,
        ScarabSoldier * 1.4,

        SandySniper * 0.6,
        LeapingSpider,
        CursedArcher * 1.1,
        CastingCorpse * 1.3,

        HuskyDuelist * 1.3,
        AncientBeast,

        RobedSkirmisher
    )

    override val lootProvider: StructureLoot =
        AbandonedShip

}