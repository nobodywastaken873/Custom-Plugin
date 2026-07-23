package me.newburyminer.customItems.structures.structure

import me.newburyminer.customItems.loot.StructureLoot
import me.newburyminer.customItems.loot.providers.structure.AbandonedShipLoot
import me.newburyminer.customItems.mobprovider.MobProvider
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

    override val lootProvider: StructureLoot = AbandonedShipLoot

}