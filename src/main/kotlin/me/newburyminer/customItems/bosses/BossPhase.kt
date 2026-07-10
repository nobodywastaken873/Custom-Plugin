package me.newburyminer.customItems.bosses

interface BossPhase<T : BossPhase<T>> {

    val entries: List<T>

    fun next(): T =
        entries.getOrNull(entries.indexOf(this) + 1) ?: entries.last()

    fun previous(): T =
        entries.getOrNull(entries.indexOf(this) - 1) ?: entries.first()

    fun isFirst() = this == entries.first()

    fun isLast() = this == entries.last()

    val ordinal: Int
        get() = entries.indexOf(this)
}