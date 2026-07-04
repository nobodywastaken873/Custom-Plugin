package me.newburyminer.customItems.structures.structure

import me.newburyminer.customItems.mobprovider.MobEntry
import me.newburyminer.customItems.mobprovider.MobProvider
import me.newburyminer.customItems.mobprovider.mobs.BasicZombie
import me.newburyminer.customItems.mobprovider.mobs.desert.CastingCorpse
import me.newburyminer.customItems.mobprovider.mobs.desert.CursedArcher
import me.newburyminer.customItems.mobprovider.mobs.desert.HuskyDuelist
import me.newburyminer.customItems.mobprovider.mobs.desert.MaskedMummy
import me.newburyminer.customItems.mobprovider.mobs.desert.PinbagCreeper
import me.newburyminer.customItems.mobprovider.mobs.desert.ScarabSoldier
import me.newburyminer.customItems.mobprovider.mobs.desert.TempleGolem
import me.newburyminer.customItems.structures.EncounterStyle
import me.newburyminer.customItems.structures.StructureDefinition
import me.newburyminer.customItems.structures.TrialSpawnerDefinition
import me.newburyminer.customItems.structures.VaultDefinition

object PyramidOfAnubis : StructureDefinition {

    override val id: String = "pyramid_of_anubis"

    override val mobProvider: MobProvider = MobProvider(
        EncounterStyle.DEFAULT.modifyElite(5),

        MaskedMummy * 1.1,
        ScarabSoldier,
        PinbagCreeper * 1.4,

        TempleGolem * 1.2,
        CursedArcher,
        CastingCorpse * 1.4,

        HuskyDuelist
    )

    override val normalSpawner: TrialSpawnerDefinition = TrialSpawnerDefinition(loot = 1)
    override val ominousSpawner: TrialSpawnerDefinition = TrialSpawnerDefinition(loot = 1)

    override val normalVault: VaultDefinition = VaultDefinition(loot = 1)
    override val ominousVault: VaultDefinition = VaultDefinition(loot = 1)
}