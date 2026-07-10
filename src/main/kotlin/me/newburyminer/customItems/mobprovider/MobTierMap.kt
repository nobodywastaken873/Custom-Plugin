package me.newburyminer.customItems.mobprovider

import kotlin.random.Random

class MobTierMap(
    val grunt: Pair<Int, Int>,
    val standard: Pair<Int, Int>,
    val elite: Pair<Int, Int>,
    val miniboss: Pair<Int, Int>
) {

    fun modifyGrunt(amount: Int): MobTierMap {
        return MobTierMap(grunt.first + amount to grunt.second + amount, standard, elite, miniboss)
    }
    fun modifyStandard(amount: Int): MobTierMap {
        return MobTierMap(grunt, standard.first + amount to standard.second + amount, elite, miniboss)
    }
    fun modifyElite(amount: Int): MobTierMap {
        return MobTierMap(grunt, standard, elite.first + amount to elite.second + amount, miniboss)
    }
    fun modifyMiniboss(amount: Int): MobTierMap {
        return MobTierMap(grunt, standard, elite, miniboss.first + amount to miniboss.second + amount)
    }

    fun getRandomTier(ctx: MobContext): MobTier {

        val gruntWeight = scale(grunt, ctx)
        val standardWeight = scale(standard, ctx) + gruntWeight
        val eliteWeight = scale(elite, ctx) + standardWeight
        val minibossWeight = scale(miniboss, ctx) + eliteWeight

        val random = Random.nextDouble() * minibossWeight

        return when {
            random < gruntWeight -> MobTier.GRUNT
            random < standardWeight -> MobTier.STANDARD
            random < eliteWeight -> MobTier.ELITE
            else -> MobTier.MINIBOSS
        }

    }

    private fun scale(range: Pair<Int, Int>, ctx: MobContext): Double {
        return range.first + (range.second - range.first) / 30.0 * ctx.difficulty
    }

}