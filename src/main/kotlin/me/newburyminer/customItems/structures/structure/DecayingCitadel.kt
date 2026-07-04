package me.newburyminer.customItems.structures.structure

import me.newburyminer.customItems.mobprovider.MobEntry
import me.newburyminer.customItems.mobprovider.MobProvider
import me.newburyminer.customItems.mobprovider.mobs.BasicZombie
import me.newburyminer.customItems.mobprovider.mobs.rocky.AnimatedRockPile
import me.newburyminer.customItems.mobprovider.mobs.rocky.HealingStone
import me.newburyminer.customItems.mobprovider.mobs.rocky.LeadenSkeleton
import me.newburyminer.customItems.mobprovider.mobs.rocky.Medusa
import me.newburyminer.customItems.mobprovider.mobs.rocky.RockSpider
import me.newburyminer.customItems.mobprovider.mobs.rocky.SmashingSpider
import me.newburyminer.customItems.mobprovider.mobs.rocky.StoneThrower
import me.newburyminer.customItems.structures.EncounterStyle
import me.newburyminer.customItems.structures.StructureDefinition
import me.newburyminer.customItems.structures.TrialSpawnerDefinition
import me.newburyminer.customItems.structures.VaultDefinition

object DecayingCitadel : StructureDefinition {

    override val id: String = "decaying_citadel"

    override val mobProvider: MobProvider = MobProvider(
        EncounterStyle.DEFAULT,

        RockSpider,
        LeadenSkeleton * 1.2,

        StoneThrower,
        SmashingSpider,
        HealingStone * 0.6,

        Medusa
    )

    override val normalSpawner: TrialSpawnerDefinition = TrialSpawnerDefinition(loot = 1)
    override val ominousSpawner: TrialSpawnerDefinition = TrialSpawnerDefinition(loot = 1)

    override val normalVault: VaultDefinition = VaultDefinition(loot = 1)
    override val ominousVault: VaultDefinition = VaultDefinition(loot = 1)
}