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

object MarbleFort : StructureDefinition {

    override val id: String = "marble_fort"

    override val mobProvider: MobProvider = MobProvider(
        EncounterStyle.STANDARD,

        PanickedSoldier * 1.5,
        Infantryman,
        WalkingExplosives,

        TrainedBeast * 1.5,
        TrappingGrenade * 0.7,
        ExperiencedAssassin,
        JoustingKnight,

        MachineGunFortification * 1.4,
        ChemicalWeaponsExpert
    )

    override val lootProvider: StructureLoot =
        AbandonedShipLoot

}