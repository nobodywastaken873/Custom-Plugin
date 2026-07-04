package me.newburyminer.customItems.structures.structure

import me.newburyminer.customItems.mobprovider.MobEntry
import me.newburyminer.customItems.mobprovider.MobProvider
import me.newburyminer.customItems.mobprovider.mobs.BasicZombie
import me.newburyminer.customItems.mobprovider.mobs.desert.*
import me.newburyminer.customItems.structures.EncounterStyle
import me.newburyminer.customItems.structures.StructureDefinition
import me.newburyminer.customItems.structures.TrialSpawnerDefinition
import me.newburyminer.customItems.structures.VaultDefinition

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

    override val normalSpawner: TrialSpawnerDefinition = TrialSpawnerDefinition(loot = 1)
    override val ominousSpawner: TrialSpawnerDefinition = TrialSpawnerDefinition(loot = 1)

    override val normalVault: VaultDefinition = VaultDefinition(loot = 1)
    override val ominousVault: VaultDefinition = VaultDefinition(loot = 1)
}