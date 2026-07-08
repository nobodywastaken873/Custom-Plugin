package me.newburyminer.customItems.bosses.rendering.floor

import me.newburyminer.customItems.bosses.rendering.shapes.Shape
import org.bukkit.Material
import org.bukkit.util.Vector

class GridShapeMesher(

    private val cellSize: Double = 0.5,

    private val material: Material

) : ShapeMesher {

    private val rasterizer = ShapeRasterizer(cellSize)

    override fun mesh(shape: Shape): Mesh {

        val grid = rasterizer.rasterize(shape)

        val tiles = mutableListOf<FloorTile>()

        for (row in 0 until grid.rows) {
            for (column in 0 until grid.columns) {

                if (!grid[row, column])
                    continue

                tiles += FloorTile(
                    center = grid.cellCenter(row, column),
                    normal = Vector(0, 1, 0),
                    up = Vector(0, 0, -1),
                    width = cellSize,
                    height = cellSize,
                    material = material
                )
            }
        }

        return Mesh(shape.version, tiles)
    }

}