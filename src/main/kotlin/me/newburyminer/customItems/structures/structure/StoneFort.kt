package me.newburyminer.customItems.structures.structure

import me.newburyminer.customItems.loot.StructureLoot
import me.newburyminer.customItems.loot.providers.structure.AbandonedShipLoot
import me.newburyminer.customItems.mobprovider.MobProvider
import me.newburyminer.customItems.mobprovider.mobs.military.ChemicalWeaponsExpert
import me.newburyminer.customItems.mobprovider.mobs.military.ExperiencedAssassin
import me.newburyminer.customItems.mobprovider.mobs.military.Infantryman
import me.newburyminer.customItems.mobprovider.mobs.military.JoustingKnight
import me.newburyminer.customItems.mobprovider.mobs.military.MachineGunFortification
import me.newburyminer.customItems.mobprovider.mobs.military.PanickedSoldier
import me.newburyminer.customItems.mobprovider.mobs.military.TrainedBeast
import me.newburyminer.customItems.mobprovider.mobs.military.TrappingGrenade
import me.newburyminer.customItems.mobprovider.mobs.military.WalkingExplosives
import me.newburyminer.customItems.structures.EncounterStyle
import me.newburyminer.customItems.structures.StructureDefinition

object StoneFort : StructureDefinition {

    override val id: String = "stone_fort"

    override val mobProvider: MobProvider = MobProvider(
        EncounterStyle.STANDARD,

        PanickedSoldier,
        Infantryman * 1.2,
        WalkingExplosives * 1.2,

        TrainedBeast * 0.6,
        TrappingGrenade,
        ExperiencedAssassin * 1.6,
        JoustingKnight * 1.3,

        MachineGunFortification,
        ChemicalWeaponsExpert * 1.4
    )

    override val lootProvider: StructureLoot =
        AbandonedShipLoot

}