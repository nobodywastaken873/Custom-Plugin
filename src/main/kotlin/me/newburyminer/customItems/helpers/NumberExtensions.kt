package me.newburyminer.customItems.helpers

fun Int.cycleUp(range: IntRange): Int {
    return if (this == range.last) range.first else this + 1
}
fun Int.cycleDown(range: IntRange): Int {
    return if (this == range.first) range.last else this - 1
}