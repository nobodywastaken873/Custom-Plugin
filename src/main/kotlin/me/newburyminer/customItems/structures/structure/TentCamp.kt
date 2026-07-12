package me.newburyminer.customItems.structures.structure

import me.newburyminer.customItems.loot.StructureLoot
import me.newburyminer.customItems.loot.providers.structure.AbandonedShip
import me.newburyminer.customItems.mobprovider.MobProvider
import me.newburyminer.customItems.mobprovider.mobs.rocky.AnimatedRockPile
import me.newburyminer.customItems.mobprovider.mobs.rocky.GraniteShell
import me.newburyminer.customItems.mobprovider.mobs.rocky.InfestedGeologist
import me.newburyminer.customItems.mobprovider.mobs.rocky.LeadenSkeleton
import me.newburyminer.customItems.mobprovider.mobs.rocky.RockGolem
import me.newburyminer.customItems.mobprovider.mobs.rocky.StoneThrower
import me.newburyminer.customItems.structures.EncounterStyle
import me.newburyminer.customItems.structures.StructureDefinition

object TentCamp : StructureDefinition {

    override val id: String = "tent_camp"

    override val mobProvider: MobProvider = MobProvider(
        EncounterStyle.DEFAULT,

        AnimatedRockPile,
        InfestedGeologist * 1.4,
        LeadenSkeleton,

        StoneThrower,
        RockGolem * 1.2,

        GraniteShell
    )

    override val lootProvider: StructureLoot =
        AbandonedShip

}