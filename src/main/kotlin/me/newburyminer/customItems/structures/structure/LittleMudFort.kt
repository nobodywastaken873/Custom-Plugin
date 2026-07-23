package me.newburyminer.customItems.structures.structure

import me.newburyminer.customItems.loot.StructureLoot
import me.newburyminer.customItems.loot.providers.structure.AbandonedShipLoot
import me.newburyminer.customItems.mobprovider.MobProvider
import me.newburyminer.customItems.mobprovider.mobs.mystic.AxedCreeper
import me.newburyminer.customItems.mobprovider.mobs.mystic.HoneyedCaster
import me.newburyminer.customItems.mobprovider.mobs.mystic.HulkingPigman
import me.newburyminer.customItems.mobprovider.mobs.mystic.PorcupineCreeper
import me.newburyminer.customItems.mobprovider.mobs.mystic.RagingPigman
import me.newburyminer.customItems.mobprovider.mobs.mystic.UndyingZombie
import me.newburyminer.customItems.structures.EncounterStyle
import me.newburyminer.customItems.structures.StructureDefinition

object LittleMudFort : StructureDefinition {

    override val id: String = "little_mud_fort"

    override val mobProvider: MobProvider = MobProvider(
        EncounterStyle.DEFAULT.modifyGrunt(-7),

        RagingPigman * 1.7,
        PorcupineCreeper,

        HoneyedCaster * 1.3,
        AxedCreeper,
        UndyingZombie,

        HulkingPigman
    )

    override val lootProvider: StructureLoot =
        AbandonedShipLoot

}