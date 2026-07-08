package me.newburyminer.customItems.bosses.rendering.shapes

data class ShapeLayer(
    val shape: Shape,
    val operation: Operation
) {
    enum class Operation {
        ADD,
        SUBTRACT
    }
}
