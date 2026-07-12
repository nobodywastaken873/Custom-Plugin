package me.newburyminer.customItems.mobprovider

import me.newburyminer.customItems.mobprovider.mobs.surface.*
import me.newburyminer.customItems.mobprovider.mobs.caves.*
import me.newburyminer.customItems.structures.EncounterStyle
import me.newburyminer.customItems.structures.structure.AbandonedShip.times

object AridLandsMobs {

    val SURFACE: MobProvider = MobProvider(
        EncounterStyle.STANDARD,

        PiglinWarrior,
        ParchedZombie * 1.2,
        ParchedSkeleton * 1.2,
        LeapingCreeper,

        DustThrower * 1.1,
        SAMInfantry,
        FirebombCreeper * 1.4,
        SwoopingMonster * 0.7,
        VenomSpider,
        FlyingTurret,
        Wind,

        EnragedBeast * 1.2,
        PiglinCommander,
        ElytraInterceptor * 0.7
    )

    val CAVES: MobProvider = MobProvider(
        EncounterStyle.STANDARD,

        LeapingCreeper * 1.4,
        FirebombCreeper * 1.4,
        SwarmingSpider,
        CaveGrenadier,
        RPGSkeleton * 0.6,

        RapidFireSkeleton * 0.8,
        BewitchingDweller * 1.2,
        CaveAssassin * 1.4,
        CaveSniper,
        CreepingCreaking * 0.8,
        HeavyZombie * 1.2,

        CaveFairy,
        CaveWind,
        CaveDweller * 1.3
    )

}