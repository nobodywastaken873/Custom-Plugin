package me.newburyminer.customItems.structures.structure

import me.newburyminer.customItems.loot.StructureLoot
import me.newburyminer.customItems.loot.providers.structure.AbandonedShipLoot
import me.newburyminer.customItems.mobprovider.MobProvider
import me.newburyminer.customItems.mobprovider.mobs.warmocean.DrownedCreature
import me.newburyminer.customItems.mobprovider.mobs.warmocean.EnragedSeaBeast
import me.newburyminer.customItems.mobprovider.mobs.warmocean.ExplosiveCoral
import me.newburyminer.customItems.mobprovider.mobs.warmocean.GiantSquid
import me.newburyminer.customItems.mobprovider.mobs.warmocean.OceanTradewind
import me.newburyminer.customItems.mobprovider.mobs.warmocean.SeaSlug
import me.newburyminer.customItems.mobprovider.mobs.warmocean.SludgeTosser
import me.newburyminer.customItems.mobprovider.mobs.warmocean.UnderseaAbomination
import me.newburyminer.customItems.mobprovider.mobs.warmocean.WhaleOilShooter
import me.newburyminer.customItems.structures.EncounterStyle
import me.newburyminer.customItems.structures.StructureDefinition

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

    override val lootProvider: StructureLoot =
        AbandonedShipLoot

}