package me.newburyminer.customItems.structures.structure

import me.newburyminer.customItems.mobprovider.MobEntry
import me.newburyminer.customItems.mobprovider.MobProvider
import me.newburyminer.customItems.mobprovider.mobs.BasicZombie
import me.newburyminer.customItems.mobprovider.mobs.military.AntiTankPersonnel
import me.newburyminer.customItems.mobprovider.mobs.military.ArmoredKnight
import me.newburyminer.customItems.mobprovider.mobs.military.AttackHound
import me.newburyminer.customItems.mobprovider.mobs.military.BattleMedic
import me.newburyminer.customItems.mobprovider.mobs.military.CharismaticCommando
import me.newburyminer.customItems.mobprovider.mobs.military.DroneSwarmer
import me.newburyminer.customItems.mobprovider.mobs.military.Infantryman
import me.newburyminer.customItems.mobprovider.mobs.military.MachineGunFortification
import me.newburyminer.customItems.mobprovider.mobs.military.MutatedBeast
import me.newburyminer.customItems.mobprovider.mobs.military.PanickedSoldier
import me.newburyminer.customItems.mobprovider.mobs.military.TowerSniper
import me.newburyminer.customItems.mobprovider.mobs.military.TrainedBeast
import me.newburyminer.customItems.mobprovider.mobs.military.TraineeFighter
import me.newburyminer.customItems.mobprovider.mobs.military.TrappingGrenade
import me.newburyminer.customItems.mobprovider.mobs.military.TrustySteed
import me.newburyminer.customItems.mobprovider.mobs.military.WalkingExplosives
import me.newburyminer.customItems.structures.EncounterStyle
import me.newburyminer.customItems.structures.StructureDefinition
import me.newburyminer.customItems.structures.TrialSpawnerDefinition
import me.newburyminer.customItems.structures.VaultDefinition

object StoneBarracks : StructureDefinition {

    override val id: String = "stone_barracks"

    override val mobProvider: MobProvider = MobProvider(
        EncounterStyle.ELITE,

        TraineeFighter * 1.3,
        Infantryman * 1.5,
        WalkingExplosives,
        AttackHound,

        ArmoredKnight * 1.3,
        TrustySteed * 1.4,
        BattleMedic * 0.8,
        TowerSniper,
        TrainedBeast,
        TrappingGrenade * 0.5,

        AntiTankPersonnel,
        CharismaticCommando * 0.7,
        MachineGunFortification * 0.8,
        DroneSwarmer,

        MutatedBeast
    )

    override val normalSpawner: TrialSpawnerDefinition = TrialSpawnerDefinition(loot = 1)
    override val ominousSpawner: TrialSpawnerDefinition = TrialSpawnerDefinition(loot = 1)

    override val normalVault: VaultDefinition = VaultDefinition(loot = 1)
    override val ominousVault: VaultDefinition = VaultDefinition(loot = 1)
}