package me.newburyminer.customItems.recipes

class GridBuilder {

    private val grid = mutableListOf<List<RecipeItemBase?>>()

    fun row(vararg items: RecipeItemBase?) {
        grid.add(items.toList())
    }

    fun build(): List<List<RecipeItemBase?>> {
        return grid.toList()
    }

}