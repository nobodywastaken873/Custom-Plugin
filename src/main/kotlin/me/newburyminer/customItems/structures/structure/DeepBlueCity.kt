package me.newburyminer.customItems.structures.structure

import me.newburyminer.customItems.mobprovider.MobEntry
import me.newburyminer.customItems.mobprovider.MobProvider
import me.newburyminer.customItems.mobprovider.mobs.BasicZombie
import me.newburyminer.customItems.mobprovider.mobs.warmocean.DeepSeaman
import me.newburyminer.customItems.mobprovider.mobs.warmocean.DrownedCreature
import me.newburyminer.customItems.mobprovider.mobs.warmocean.EnragedSeaBeast
import me.newburyminer.customItems.mobprovider.mobs.warmocean.ExplosiveCoral
import me.newburyminer.customItems.mobprovider.mobs.warmocean.HealingStingray
import me.newburyminer.customItems.mobprovider.mobs.warmocean.MassiveMermaid
import me.newburyminer.customItems.mobprovider.mobs.warmocean.MutatedShark
import me.newburyminer.customItems.mobprovider.mobs.warmocean.OceanTradewind
import me.newburyminer.customItems.mobprovider.mobs.warmocean.SeaweedWrapper
import me.newburyminer.customItems.mobprovider.mobs.warmocean.SludgeTosser
import me.newburyminer.customItems.mobprovider.mobs.warmocean.WindGod
import me.newburyminer.customItems.structures.EncounterStyle
import me.newburyminer.customItems.structures.StructureDefinition
import me.newburyminer.customItems.structures.TrialSpawnerDefinition
import me.newburyminer.customItems.structures.VaultDefinition

object DeepBlueCity : StructureDefinition {

    override val id: String = "deep_blue_city"

    override val mobProvider: MobProvider = MobProvider(
        EncounterStyle.ELITE,

        DrownedCreature * 1.2,
        SeaweedWrapper,
        SludgeTosser * 0.8,

        ExplosiveCoral,
        HealingStingray * 0.8,
        EnragedSeaBeast,
        DeepSeaman * 1.5,
        OceanTradewind * 1.4,

        MassiveMermaid * 1.2,
        WindGod,

        MutatedShark
    )

    override val normalSpawner: TrialSpawnerDefinition = TrialSpawnerDefinition(loot = 1)
    override val ominousSpawner: TrialSpawnerDefinition = TrialSpawnerDefinition(loot = 1)

    override val normalVault: VaultDefinition = VaultDefinition(loot = 1)
    override val ominousVault: VaultDefinition = VaultDefinition(loot = 1)
}