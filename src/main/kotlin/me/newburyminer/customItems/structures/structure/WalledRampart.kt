package me.newburyminer.customItems.structures.structure

import me.newburyminer.customItems.loot.StructureLoot
import me.newburyminer.customItems.loot.providers.structure.AbandonedShip
import me.newburyminer.customItems.mobprovider.MobProvider
import me.newburyminer.customItems.mobprovider.mobs.military.AntiTankPersonnel
import me.newburyminer.customItems.mobprovider.mobs.military.ArmoredKnight
import me.newburyminer.customItems.mobprovider.mobs.military.BattleMedic
import me.newburyminer.customItems.mobprovider.mobs.military.CharismaticCommando
import me.newburyminer.customItems.mobprovider.mobs.military.DroneSwarmer
import me.newburyminer.customItems.mobprovider.mobs.military.Infantryman
import me.newburyminer.customItems.mobprovider.mobs.military.PanickedSoldier
import me.newburyminer.customItems.mobprovider.mobs.military.TowerSniper
import me.newburyminer.customItems.mobprovider.mobs.military.TrainedBeast
import me.newburyminer.customItems.mobprovider.mobs.military.TraineeFighter
import me.newburyminer.customItems.mobprovider.mobs.military.TrappingGrenade
import me.newburyminer.customItems.mobprovider.mobs.military.TrustySteed
import me.newburyminer.customItems.mobprovider.mobs.military.WalkingExplosives
import me.newburyminer.customItems.structures.EncounterStyle
import me.newburyminer.customItems.structures.StructureDefinition

object WalledRampart : StructureDefinition {

    override val id: String = "walled_rampart"

    override val mobProvider: MobProvider = MobProvider(
        EncounterStyle.STANDARD.modifyElite(4),

        TraineeFighter * 1.4,
        Infantryman,
        PanickedSoldier * 0.7,
        WalkingExplosives,

        ArmoredKnight * 1.3,
        TrustySteed,
        BattleMedic * 0.8,
        TowerSniper * 0.8,
        TrainedBeast * 1.2,
        TrappingGrenade * 0.6,

        AntiTankPersonnel,
        CharismaticCommando * 1.2,
        DroneSwarmer * 0.8
    )

    override val lootProvider: StructureLoot =
        AbandonedShip

}