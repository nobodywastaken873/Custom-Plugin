package me.newburyminer.customItems.bosses.rendering.floor

import me.newburyminer.customItems.bosses.rendering.shapes.Shape
import kotlin.math.ceil

class ShapeRasterizer(
    private val cellSize: Double
) {

    fun rasterize(shape: Shape): BooleanGrid {

        val bounds = shape.bounds

        val rows = ceil(bounds.widthX / cellSize).toInt()
        val columns = ceil(bounds.widthZ / cellSize).toInt()

        val grid = BooleanGrid(
            origin = bounds.min.clone(),
            rows = rows,
            columns = columns,
            cellSize = cellSize
        )

        for (row in 0 until rows) {
            for (column in 0 until columns) {

                grid[row, column] =
                    shape.contains(grid.cellCenter(row, column))
            }
        }

        return grid
    }

}