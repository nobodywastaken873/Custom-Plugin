package me.newburyminer.customItems.structures.structure

import me.newburyminer.customItems.mobprovider.MobEntry
import me.newburyminer.customItems.mobprovider.MobProvider
import me.newburyminer.customItems.mobprovider.mobs.BasicZombie
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
import me.newburyminer.customItems.structures.TrialSpawnerDefinition
import me.newburyminer.customItems.structures.VaultDefinition

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

    override val normalSpawner: TrialSpawnerDefinition = TrialSpawnerDefinition(loot = 1)
    override val ominousSpawner: TrialSpawnerDefinition = TrialSpawnerDefinition(loot = 1)

    override val normalVault: VaultDefinition = VaultDefinition(loot = 1)
    override val ominousVault: VaultDefinition = VaultDefinition(loot = 1)
}