package me.newburyminer.customItems.systems.materials

import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.setTag
import org.bukkit.entity.Player

object MaterialSystem {

    fun getMaterials(player: Player): MaterialCollection {
        val playerString = player.getTag<String>("materialcollection") ?: ""
        val collection = MaterialCollection.deserialize(playerString)
        return collection
    }

    fun getMaterialSubset(player: Player, subset: MaterialCategory): MaterialCollection {
        val collection = getMaterials(player)
        return collection.getSubset(subset)
    }

    fun addMaterials(player: Player, toAdd: MaterialCollection) {
        val materials = getMaterials(player)
        materials.add(toAdd)
        player.setTag("materialcollection", materials.serialize())
    }

    fun removeMaterials(player: Player, toRemove: MaterialCollection) {
        val materials = getMaterials(player)
        materials.remove(toRemove)
        player.setTag("materialcollection", materials.serialize())
    }

    fun hasMaterials(player: Player, toCheck: MaterialCollection): Boolean {
        val playerMaterials = getMaterials(player)
        return playerMaterials.contains(toCheck)
    }

}