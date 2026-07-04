package me.newburyminer.customItems.structures.structure

import me.newburyminer.customItems.mobprovider.MobEntry
import me.newburyminer.customItems.mobprovider.MobProvider
import me.newburyminer.customItems.mobprovider.mobs.BasicZombie
import me.newburyminer.customItems.mobprovider.mobs.blackstone.BlackstoneHermit
import me.newburyminer.customItems.mobprovider.mobs.blackstone.BlazingCreeper
import me.newburyminer.customItems.mobprovider.mobs.blackstone.BlazingTurret
import me.newburyminer.customItems.mobprovider.mobs.blackstone.CastingBones
import me.newburyminer.customItems.mobprovider.mobs.blackstone.DarkDuelist
import me.newburyminer.customItems.mobprovider.mobs.blackstone.EmblazenedSkeleton
import me.newburyminer.customItems.mobprovider.mobs.blackstone.SoulMage
import me.newburyminer.customItems.mobprovider.mobs.blackstone.WitherWarrior
import me.newburyminer.customItems.structures.EncounterStyle
import me.newburyminer.customItems.structures.StructureDefinition
import me.newburyminer.customItems.structures.TrialSpawnerDefinition
import me.newburyminer.customItems.structures.VaultDefinition

object BlackTowers : StructureDefinition {

    override val id: String = "black_towers"

    override val mobProvider: MobProvider = MobProvider(
        EncounterStyle.STANDARD.modifyStandard(5),

        BlazingTurret,
        BlazingCreeper * 1.3,
        CastingBones,

        DarkDuelist * 1.7,
        EmblazenedSkeleton * 1.2,
        BlackstoneHermit,
        WitherWarrior * 0.6,

        SoulMage
    )

    override val normalSpawner: TrialSpawnerDefinition = TrialSpawnerDefinition(loot = 1)
    override val ominousSpawner: TrialSpawnerDefinition = TrialSpawnerDefinition(loot = 1)

    override val normalVault: VaultDefinition = VaultDefinition(loot = 1)
    override val ominousVault: VaultDefinition = VaultDefinition(loot = 1)
}