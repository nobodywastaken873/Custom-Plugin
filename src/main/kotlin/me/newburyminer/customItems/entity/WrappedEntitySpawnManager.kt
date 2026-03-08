package me.newburyminer.customItems.entity

object WrappedEntitySpawnManager {
    /*@EventHandler fun onEntitySpawn(e: CreatureSpawnEvent) {

        if (e.entity.world != CustomItems.aridWorld) return

        val (difficulty, spawnType) = getDifficulty(e)

        var spawnRate = 0.25
        when (spawnType) {
            //CustomSpawnType.NATURAL -> {}
            //CustomSpawnType.NORMAL_SPAWNER -> {spawnRate *= 0.5}
            //CustomSpawnType.OMINOUS_SPAWNER -> {spawnRate += 0.25}
        }

        if (Math.random() > spawnRate) {e.isCancelled = true; return}

        val customSpawnPercent = difficulty.pow(0.86).coerceAtMost(100.0)
        val customEntityType =
            if (Utils.randomPercent(customSpawnPercent)) {
                //weights[e.entityType]?.next() ?: return
            } else {
                //defaults[e.entityType] ?: return
            }
        //conversionMap[customEntityType]?.convert(e.entity)

    }*/

    /*private fun getDifficulty(e: CreatureSpawnEvent): Pair<Double, CustomSpawnType> {

        var difficulty = e.location.world.getDifficultyIndex(e.location)
        var spawnType = CustomSpawnType.NATURAL

        if (e.entity.isSilent && !e.entity.hasAI()) {

            // manipulate difficulty here
            difficulty *= 2.0
            spawnType = CustomSpawnType.OMINOUS_SPAWNER
            e.entity.isSilent = false
            e.entity.setAI(true)

        } else if (!e.entity.hasAI()) {

            // manipulate difficulty here
            difficulty *= 1.5
            spawnType = CustomSpawnType.NORMAL_SPAWNER
            e.entity.setAI(true)

        }

        return difficulty to spawnType
    }*/
}