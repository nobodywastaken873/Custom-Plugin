package me.newburyminer.customItems.structures

import me.newburyminer.customItems.mobprovider.MobTierMap

object EncounterStyle {

    val SWARM: MobTierMap = MobTierMap(
        grunt = 65 to 40,
        standard = 33 to 53,
        elite = 2 to 7,
        miniboss = 0 to 0
    )

    val DEFAULT: MobTierMap = MobTierMap(
        grunt = 62 to 35,
        standard = 35 to 56,
        elite = 3 to 11,
        miniboss = 0 to 0
    )

    val STANDARD: MobTierMap = MobTierMap(
        grunt = 45 to 25,
        standard = 50 to 60,
        elite = 5 to 15,
        miniboss = 0 to 0
    )

    val ELITE: MobTierMap = MobTierMap(
        grunt = 55 to 25,
        standard = 40 to 50,
        elite = 5 to 21,
        miniboss = 0 to 4
    )

}