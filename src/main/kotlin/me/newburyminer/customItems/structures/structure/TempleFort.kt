package me.newburyminer.customItems.structures.structure

import me.newburyminer.customItems.mobprovider.MobEntry
import me.newburyminer.customItems.mobprovider.MobProvider
import me.newburyminer.customItems.mobprovider.mobs.BasicZombie
import me.newburyminer.customItems.mobprovider.mobs.mystic.AxedCreeper
import me.newburyminer.customItems.mobprovider.mobs.mystic.BreakingBombadier
import me.newburyminer.customItems.mobprovider.mobs.mystic.HealerMage
import me.newburyminer.customItems.mobprovider.mobs.mystic.HoneyedCaster
import me.newburyminer.customItems.mobprovider.mobs.mystic.KingBee
import me.newburyminer.customItems.mobprovider.mobs.mystic.MudGolem
import me.newburyminer.customItems.mobprovider.mobs.mystic.PollenLadenBee
import me.newburyminer.customItems.mobprovider.mobs.mystic.QueenBee
import me.newburyminer.customItems.mobprovider.mobs.mystic.RagingPigman
import me.newburyminer.customItems.structures.EncounterStyle
import me.newburyminer.customItems.structures.StructureDefinition
import me.newburyminer.customItems.structures.TrialSpawnerDefinition
import me.newburyminer.customItems.structures.VaultDefinition

object TempleFort : StructureDefinition {

    override val id: String = "temple_fort"

    override val mobProvider: MobProvider = MobProvider(
        EncounterStyle.ELITE,

        PollenLadenBee,
        RagingPigman * 1.2,

        BreakingBombadier * 1.2,
        HealerMage * 0.7,
        HoneyedCaster,
        AxedCreeper,

        MudGolem * 1.2,
        KingBee,

        QueenBee
    )

    override val normalSpawner: TrialSpawnerDefinition = TrialSpawnerDefinition(loot = 1)
    override val ominousSpawner: TrialSpawnerDefinition = TrialSpawnerDefinition(loot = 1)

    override val normalVault: VaultDefinition = VaultDefinition(loot = 1)
    override val ominousVault: VaultDefinition = VaultDefinition(loot = 1)
}