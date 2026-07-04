package me.newburyminer.customItems.structures.structure

import me.newburyminer.customItems.mobprovider.MobEntry
import me.newburyminer.customItems.mobprovider.MobProvider
import me.newburyminer.customItems.mobprovider.mobs.BasicZombie
import me.newburyminer.customItems.mobprovider.mobs.rocky.AnimatedRockPile
import me.newburyminer.customItems.mobprovider.mobs.rocky.CliffProwler
import me.newburyminer.customItems.mobprovider.mobs.rocky.GraniteShell
import me.newburyminer.customItems.mobprovider.mobs.rocky.HealingStone
import me.newburyminer.customItems.mobprovider.mobs.rocky.InfestedGeologist
import me.newburyminer.customItems.mobprovider.mobs.rocky.LeadenSkeleton
import me.newburyminer.customItems.mobprovider.mobs.rocky.RockGolem
import me.newburyminer.customItems.mobprovider.mobs.rocky.RockSpider
import me.newburyminer.customItems.mobprovider.mobs.rocky.StoneThrower
import me.newburyminer.customItems.structures.EncounterStyle
import me.newburyminer.customItems.structures.StructureDefinition
import me.newburyminer.customItems.structures.TrialSpawnerDefinition
import me.newburyminer.customItems.structures.VaultDefinition

object DeterioratedBridge : StructureDefinition {

    override val id: String = "deteriorated_bridge"

    override val mobProvider: MobProvider = MobProvider(
        EncounterStyle.DEFAULT,

        InfestedGeologist,
        LeadenSkeleton * 1.1,
        RockSpider * 1.4,

        StoneThrower,
        HealingStone * 0.7,
        RockGolem * 1.2,

        GraniteShell,

        CliffProwler
    )

    override val normalSpawner: TrialSpawnerDefinition = TrialSpawnerDefinition(loot = 1)
    override val ominousSpawner: TrialSpawnerDefinition = TrialSpawnerDefinition(loot = 1)

    override val normalVault: VaultDefinition = VaultDefinition(loot = 1)
    override val ominousVault: VaultDefinition = VaultDefinition(loot = 1)
}