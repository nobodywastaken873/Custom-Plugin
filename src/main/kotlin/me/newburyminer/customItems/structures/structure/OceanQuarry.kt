package me.newburyminer.customItems.structures.structure

import me.newburyminer.customItems.mobprovider.MobEntry
import me.newburyminer.customItems.mobprovider.MobProvider
import me.newburyminer.customItems.mobprovider.mobs.BasicZombie
import me.newburyminer.customItems.mobprovider.mobs.warmocean.DrownedCreature
import me.newburyminer.customItems.mobprovider.mobs.warmocean.EnragedSeaBeast
import me.newburyminer.customItems.mobprovider.mobs.warmocean.ExplosiveCoral
import me.newburyminer.customItems.mobprovider.mobs.warmocean.GiantSquid
import me.newburyminer.customItems.mobprovider.mobs.warmocean.OceanTradewind
import me.newburyminer.customItems.mobprovider.mobs.warmocean.SeaSlug
import me.newburyminer.customItems.mobprovider.mobs.warmocean.SludgeTosser
import me.newburyminer.customItems.mobprovider.mobs.warmocean.UnderseaAbomination
import me.newburyminer.customItems.mobprovider.mobs.warmocean.WhaleOilShooter
import me.newburyminer.customItems.mobprovider.mobs.warmocean.WindGod
import me.newburyminer.customItems.structures.EncounterStyle
import me.newburyminer.customItems.structures.StructureDefinition
import me.newburyminer.customItems.structures.TrialSpawnerDefinition
import me.newburyminer.customItems.structures.VaultDefinition

object OceanQuarry : StructureDefinition {

    override val id: String = "ocean_quarry"

    override val mobProvider: MobProvider = MobProvider(
        EncounterStyle.STANDARD,

        SeaSlug,
        DrownedCreature * 1.3,
        SludgeTosser,

        ExplosiveCoral * 1.2,
        EnragedSeaBeast,
        OceanTradewind * 1.5,
        UnderseaAbomination,

        WhaleOilShooter * 1.2,
        GiantSquid
    )

    override val normalSpawner: TrialSpawnerDefinition = TrialSpawnerDefinition(loot = 1)
    override val ominousSpawner: TrialSpawnerDefinition = TrialSpawnerDefinition(loot = 1)

    override val normalVault: VaultDefinition = VaultDefinition(loot = 1)
    override val ominousVault: VaultDefinition = VaultDefinition(loot = 1)
}