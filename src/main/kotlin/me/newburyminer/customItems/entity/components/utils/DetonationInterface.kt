package me.newburyminer.customItems.entity.components.utils

import me.newburyminer.customItems.Utils.Companion.setTag
import org.bukkit.entity.Entity

interface DetonationInterface {

    fun detonate(entity: Entity, power: Float, setFire: Boolean, breakBlocks: Boolean = false) {
        if (!entity.isValid) return
        entity.setTag("exploding", true)
        entity.location.world.createExplosion(
            entity,
            entity.location,
            power,
            setFire,
            breakBlocks
        )
        entity.remove()
    }

}