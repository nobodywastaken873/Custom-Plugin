package me.newburyminer.customItems.structures.structure

import me.newburyminer.customItems.loot.StructureLoot
import me.newburyminer.customItems.loot.providers.structure.AbandonedShip
import me.newburyminer.customItems.mobprovider.MobProvider
import me.newburyminer.customItems.mobprovider.mobs.blackstone.BlazingCreeper
import me.newburyminer.customItems.mobprovider.mobs.blackstone.CastingBones
import me.newburyminer.customItems.mobprovider.mobs.blackstone.DarkDuelist
import me.newburyminer.customItems.mobprovider.mobs.blackstone.EmblazenedSkeleton
import me.newburyminer.customItems.mobprovider.mobs.blackstone.HyperReactiveCreeper
import me.newburyminer.customItems.mobprovider.mobs.blackstone.MagmaBrute
import me.newburyminer.customItems.mobprovider.mobs.blackstone.ReplicatingCube
import me.newburyminer.customItems.mobprovider.mobs.blackstone.SwollenSkeleton
import me.newburyminer.customItems.mobprovider.mobs.blackstone.WitherWarrior
import me.newburyminer.customItems.structures.EncounterStyle
import me.newburyminer.customItems.structures.StructureDefinition

object BlackstoneTower : StructureDefinition {

    override val id: String = "blackstone_tower"

    override val mobProvider: MobProvider = MobProvider(
        EncounterStyle.STANDARD.modifyElite(2),

        CastingBones,
        SwollenSkeleton * 1.5,
        BlazingCreeper,

        DarkDuelist * 1.2,
        EmblazenedSkeleton * 0.5,
        WitherWarrior * 1.5,
        ReplicatingCube,

        HyperReactiveCreeper,
        MagmaBrute * 1.3
    )

    override val lootProvider: StructureLoot =
        AbandonedShip

}