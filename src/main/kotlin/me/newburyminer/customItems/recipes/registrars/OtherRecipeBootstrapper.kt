package me.newburyminer.customItems.recipes.registrars

import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.recipes.RecipeBootstrapper
import me.newburyminer.customItems.recipes.RecipeRegistry
import me.newburyminer.customItems.recipes.RecipeType
import org.bukkit.Material
import org.bukkit.MusicInstrument
import org.bukkit.potion.PotionType

object OtherRecipeBootstrapper: RecipeBootstrapper {
    override val recipeType: RecipeType = RecipeType.OTHER
    override fun bootstrap() {
        recipe {
            grid {
                row(null, null, null, null, null)
                row(null, null, null, null, null)
                row(null, null, item(Material.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE).ench("DU1"), null, null)
                row(null, null, null, null, null)
                row(null, null, null, null, null)
            }
            result(CustomItem.WARDEN_SPAWNER)
        }
    }
}