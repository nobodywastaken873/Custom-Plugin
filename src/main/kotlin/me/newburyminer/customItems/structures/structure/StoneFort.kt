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

    override val normalSpawner: TrialSpawnerDefinition = TrialSpawnerDefinition(loot = 1)
    override val ominousSpawner: TrialSpawnerDefinition = TrialSpawnerDefinition(loot = 1)

    override val normalVault: VaultDefinition = VaultDefinition(loot = 1)
    override val ominousVault: VaultDefinition = VaultDefinition(loot = 1)
}