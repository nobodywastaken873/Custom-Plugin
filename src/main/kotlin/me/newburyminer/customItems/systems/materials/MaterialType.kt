package me.newburyminer.customItems.systems.materials

import org.bukkit.Material

enum class MaterialType(val icon: Material, vararg val categories: MaterialCategory) {
    OAK_WOOD(Material.OAK_PLANKS, MaterialCategory.REDSTONE, MaterialCategory.BUILDING),
    REDSTONE(Material.REDSTONE, MaterialCategory.REDSTONE),
    STONE(Material.STONE, MaterialCategory.REDSTONE, MaterialCategory.BUILDING),
    COBBLESTONE(Material.COBBLESTONE, MaterialCategory.REDSTONE, MaterialCategory.BUILDING),
    GOLD(Material.GOLD_INGOT, MaterialCategory.REDSTONE),
    IRON(Material.IRON_INGOT, MaterialCategory.REDSTONE),
    STRING(Material.STRING, MaterialCategory.REDSTONE),
    SLIME(Material.SLIME_BALL, MaterialCategory.REDSTONE),
    QUARTZ(Material.QUARTZ, MaterialCategory.REDSTONE),
    TNT(Material.TNT, MaterialCategory.REDSTONE)


    ;

    fun cleanName() {

    }

    companion object {
        fun getMaterials(category: MaterialCategory): List<MaterialType> {
            val allEntries = MaterialType.entries.toMutableList()
            allEntries.removeIf { category !in it.categories }
            return allEntries.toList()
        }
    }

}