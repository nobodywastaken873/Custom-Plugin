package me.newburyminer.customItems.helpers

class RandomSelector<T>(vararg elems: Pair<T, Int>) {
    private val weightedList: MutableList<Pair<T, Int>> = mutableListOf()
    private var totalWeights = 0
    init {
        for (elem in elems) {
            weightedList.add(elem)
            totalWeights += elem.second
        }
    }

    fun next(): T {
        val random = Math.random() * totalWeights
        var currentTotal = 0
        for (elem in weightedList) {
            currentTotal += elem.second
            if (currentTotal > random) {
                return elem.first
            }
        }
        return weightedList.first().first
    }

    fun add(elem: Pair<T, Int>) {
        totalWeights += elem.second
        weightedList.add(elem)
    }
}