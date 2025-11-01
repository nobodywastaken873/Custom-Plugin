package me.newburyminer.customItems.helpers

class DoubleRange(first: Double, second: Double, buffer: Double = 0.0) {
    private val range: ClosedRange<Double> = if (first <= second) (first - buffer)..(second + buffer) else (second - buffer)..(first + buffer)

    operator fun contains(x: Double): Boolean {
        return range.contains(x)
    }
}