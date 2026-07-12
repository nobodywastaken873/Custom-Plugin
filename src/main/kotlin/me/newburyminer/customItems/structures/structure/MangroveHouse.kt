package me.newburyminer.customItems.structures.structure

import me.newburyminer.customItems.loot.StructureLoot
import me.newburyminer.customItems.loot.providers.structure.AbandonedShip
import me.newburyminer.customItems.mobprovider.MobProvider
import me.newburyminer.customItems.mobprovider.mobs.mystic.AxedCreeper
import me.newburyminer.customItems.mobprovider.mobs.mystic.HealerMage
import me.newburyminer.customItems.mobprovider.mobs.mystic.KingBee
import me.newburyminer.customItems.mobprovider.mobs.mystic.PollenLadenBee
import me.newburyminer.customItems.mobprovider.mobs.mystic.PorcupineCreeper
import me.newburyminer.customItems.mobprovider.mobs.mystic.RagingPigman
import me.newburyminer.customItems.mobprovider.mobs.mystic.UndyingZombie
import me.newburyminer.customItems.mobprovider.mobs.mystic.UnstableHornet
import me.newburyminer.customItems.structures.EncounterStyle
import me.newburyminer.customItems.structures.StructureDefinition

object MangroveHouse : StructureDefinition {

    override val id: String = "mangrove_house"

    override val mobProvider: MobProvider = MobProvider(
        EncounterStyle.SWARM,

        UnstableHornet * 1.2,
        PorcupineCreeper * 0.8,
        PollenLadenBee,
        RagingPigman * 1.4,

        AxedCreeper,
        HealerMage * 0.7,
        UndyingZombie * 1.1,

        KingBee
    )

    override val lootProvider: StructureLoot =
        AbandonedShip

}