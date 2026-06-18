package me.newburyminer.customItems.entity.hiteffects.effect

import me.newburyminer.customItems.entity.hiteffects.HitEffect
import me.newburyminer.customItems.entity.hiteffects.HitEffectDeserialization
import me.newburyminer.customItems.entity.hiteffects.HitEffectType
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector

class BlockPlaceApply(val block: Material, val transforms: List<Vector> = listOf(Vector(0, 0, 0))): HitEffect {

    override fun apply(victim: LivingEntity, damager: Entity, sourceLoc: Location?) {
        transforms.forEach {
            val center = victim.location.toBlockLocation().add(it)
            if (center.block.isPassable)
                center.block.type = block
        }
    }

    override fun serialize(): Map<String, Any> {
        val transformString = transforms.joinToString(";") {
            "${it.x},${it.y},${it.z}"
        }
        return mapOf(
            "block" to block.name,
            "transforms" to transformString
        )
    }
    companion object: HitEffectDeserialization {
        override val componentType: HitEffectType = HitEffectType.BLOCK_PLACE
        override fun deserialize(map: Map<String, Any>): HitEffect {
            val newBlock = Material.valueOf(map["block"] as String)
            val newTransforms = (map["transforms"] as String)
                .split(";")
                .map { coordString ->
                    val coords = coordString
                        .split(",")
                        .map { it.asInt() }
                    Vector(coords[0], coords[1], coords[2])
                }
            return BlockPlaceApply(newBlock, newTransforms)
        }
    }
}