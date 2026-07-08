package me.newburyminer.customItems.bosses.rendering.floor

import me.newburyminer.customItems.bosses.rendering.shapes.Shape
import org.bukkit.Material
import org.bukkit.util.Vector

class GreedyShapeMesher(
    private val cellSize: Double = 0.25,
    var material: Material
) : ShapeMesher {

    private val rasterizer = ShapeRasterizer(cellSize)

    override fun mesh(shape: Shape): Mesh {

        val grid = rasterizer.rasterize(shape)

        val used = Array(grid.rows) {
            BooleanArray(grid.columns)
        }

        val tiles = mutableListOf<FloorTile>()

        for (row in 0 until grid.rows) {
            for (column in 0 until grid.columns) {

                if (!grid[row, column]) continue
                if (used[row][column]) continue

                var width = 1

                while (
                    column + width < grid.columns &&
                    grid[row, column + width] &&
                    !used[row][column + width]
                ) {
                    width++
                }

                var height = 1

                outer@ while (row + height < grid.rows) {

                    for (x in 0 until width) {

                        if (
                            !grid[row + height, column + x] ||
                            used[row + height][column + x]
                        ) {
                            break@outer
                        }

                    }

                    height++
                }

                for (r in row until row + height) {
                    for (c in column until column + width) {
                        used[r][c] = true
                    }
                }

                val center = Vector(
                    grid.origin.x + (column + width / 2.0) * cellSize,
                    shape.y,
                    grid.origin.z + (row + height / 2.0) * cellSize
                )

                tiles += FloorTile(
                    center,
                    Vector(0, 1, 0),
                    Vector(0, 0, -1),
                    width * cellSize,
                    height * cellSize,
                    material
                )
            }
        }

        return Mesh(shape.version, tiles)
    }
}