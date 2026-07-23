package me.newburyminer.customItems.structures.structure

import me.newburyminer.customItems.loot.StructureLoot
import me.newburyminer.customItems.loot.providers.structure.AbandonedShipLoot
import me.newburyminer.customItems.mobprovider.MobProvider
import me.newburyminer.customItems.mobprovider.mobs.blackstone.BlackstoneHermit
import me.newburyminer.customItems.mobprovider.mobs.blackstone.BlazingCreeper
import me.newburyminer.customItems.mobprovider.mobs.blackstone.CastingBones
import me.newburyminer.customItems.mobprovider.mobs.blackstone.CrushingCube
import me.newburyminer.customItems.mobprovider.mobs.blackstone.DarkDuelist
import me.newburyminer.customItems.mobprovider.mobs.blackstone.EmblazenedSkeleton
import me.newburyminer.customItems.mobprovider.mobs.blackstone.FireBombardier
import me.newburyminer.customItems.mobprovider.mobs.blackstone.MagmaBrute
import me.newburyminer.customItems.mobprovider.mobs.blackstone.ReplicatingCube
import me.newburyminer.customItems.mobprovider.mobs.blackstone.SoulMage
import me.newburyminer.customItems.mobprovider.mobs.blackstone.SupermassiveCube
import me.newburyminer.customItems.mobprovider.mobs.blackstone.SwollenSkeleton
import me.newburyminer.customItems.mobprovider.mobs.blackstone.TowerBlaze
import me.newburyminer.customItems.structures.EncounterStyle
import me.newburyminer.customItems.structures.StructureDefinition

object DarkFortress : StructureDefinition {

    override val id: String = "dark_fortress"

    override val mobProvider: MobProvider = MobProvider(
        EncounterStyle.ELITE,

        SwollenSkeleton * 1.2,
        CastingBones,
        BlazingCreeper,

        CrushingCube * 1.1,
        EmblazenedSkeleton,
        BlackstoneHermit,
        FireBombardier * 1.2,
        ReplicatingCube,
        DarkDuelist * 0.7,

        SupermassiveCube,
        SoulMage,
        MagmaBrute * 0.6,

        TowerBlaze
    )

    override val lootProvider: StructureLoot =
        AbandonedShipLoot

}