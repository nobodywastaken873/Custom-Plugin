package me.newburyminer.customItems.bosses.rendering.floor

import org.bukkit.util.Vector

class BooleanGrid(

    val origin: Vector,

    val rows: Int,

    val columns: Int,

    val cellSize: Double

) {

    private val cells = Array(rows) {
        BooleanArray(columns)
    }

    operator fun get(row: Int, column: Int): Boolean {

        if (row !in 0 until rows) return false
        if (column !in 0 until columns) return false

        return cells[row][column]
    }

    operator fun set(row: Int, column: Int, value: Boolean) {

        if (row !in 0 until rows) return
        if (column !in 0 until columns) return

        cells[row][column] = value
    }

    fun cellCenter(row: Int, column: Int): Vector {

        return origin.clone().add(
            Vector(
                (column + 0.5) * cellSize,
                0.0,
                (row + 0.5) * cellSize
            )
        )
    }

}