package me.newburyminer.customItems.structures.structure

import me.newburyminer.customItems.loot.StructureLoot
import me.newburyminer.customItems.loot.providers.structure.AbandonedShip
import me.newburyminer.customItems.mobprovider.MobProvider
import me.newburyminer.customItems.mobprovider.mobs.military.ArmoredKnight
import me.newburyminer.customItems.mobprovider.mobs.military.AttackHound
import me.newburyminer.customItems.mobprovider.mobs.military.ChemicalWeaponsExpert
import me.newburyminer.customItems.mobprovider.mobs.military.ExperiencedAssassin
import me.newburyminer.customItems.mobprovider.mobs.military.Infantryman
import me.newburyminer.customItems.mobprovider.mobs.military.SneakingCreaking
import me.newburyminer.customItems.mobprovider.mobs.military.TrappingGrenade
import me.newburyminer.customItems.mobprovider.mobs.military.WalkingExplosives
import me.newburyminer.customItems.structures.EncounterStyle
import me.newburyminer.customItems.structures.StructureDefinition

object WroughtFort : StructureDefinition {

    override val id: String = "wrought_fort"

    override val mobProvider: MobProvider = MobProvider(
        EncounterStyle.DEFAULT,

        AttackHound * 1.2,
        Infantryman * 1.4,
        WalkingExplosives,

        ArmoredKnight * 1.2,
        ExperiencedAssassin,
        SneakingCreaking * 0.8,
        TrappingGrenade,

        ChemicalWeaponsExpert
    )

    override val lootProvider: StructureLoot =
        AbandonedShip

}