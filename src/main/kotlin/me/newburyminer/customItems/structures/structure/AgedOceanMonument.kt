package me.newburyminer.customItems.structures.structure

import me.newburyminer.customItems.mobprovider.MobEntry
import me.newburyminer.customItems.mobprovider.MobProvider
import me.newburyminer.customItems.mobprovider.mobs.BasicZombie
import me.newburyminer.customItems.mobprovider.mobs.warmocean.DeepSeaman
import me.newburyminer.customItems.mobprovider.mobs.warmocean.DrownedCreature
import me.newburyminer.customItems.mobprovider.mobs.warmocean.EnragedSeaBeast
import me.newburyminer.customItems.mobprovider.mobs.warmocean.GiantSquid
import me.newburyminer.customItems.mobprovider.mobs.warmocean.HealingStingray
import me.newburyminer.customItems.mobprovider.mobs.warmocean.MassiveMermaid
import me.newburyminer.customItems.mobprovider.mobs.warmocean.SeaSlug
import me.newburyminer.customItems.mobprovider.mobs.warmocean.SeaweedWrapper
import me.newburyminer.customItems.mobprovider.mobs.warmocean.UnderseaAbomination
import me.newburyminer.customItems.structures.EncounterStyle
import me.newburyminer.customItems.structures.StructureDefinition
import me.newburyminer.customItems.structures.TrialSpawnerDefinition
import me.newburyminer.customItems.structures.VaultDefinition

object AgedOceanMonument : StructureDefinition {

    override val id: String = "aged_ocean_monument"

    override val mobProvider: MobProvider = MobProvider(
        EncounterStyle.DEFAULT,

        SeaSlug * 1.2,
        SeaweedWrapper * 0.9,
        DrownedCreature * 1.4,

        HealingStingray * 0.8,
        DeepSeaman * 1.2,
        EnragedSeaBeast,
        UnderseaAbomination,

        MassiveMermaid,
        GiantSquid * 1.2
    )

    override val normalSpawner: TrialSpawnerDefinition = TrialSpawnerDefinition(loot = 1)
    override val ominousSpawner: TrialSpawnerDefinition = TrialSpawnerDefinition(loot = 1)

    override val normalVault: VaultDefinition = VaultDefinition(loot = 1)
    override val ominousVault: VaultDefinition = VaultDefinition(loot = 1)
}