package me.newburyminer.customItems.bosses

class ActionTimeline {

    private val timeline = mutableMapOf< Int, MutableList<() -> Unit> >()

    private var timer = 0
    fun tick() {
        val events = timeline[timer].orEmpty()
        events.forEach { it() }
        timer++
    }

    private fun lastTick(): Int {
        return timeline.keys.maxOfOrNull { it } ?: 0
    }

    fun at(time: Int, value: () -> Unit) {
        timeline.getOrPut(time) { mutableListOf() }.add(value)
    }

    fun repeat(start: Int, end: Int, value: () -> Unit) {
        for (i in start until end) {
            timeline.getOrPut(i) { mutableListOf() }.add(value)
        }
    }

    fun every(start: Int, end: Int, delay: Int, value: () -> Unit) {
        val correctedDelay = if (delay != 0) delay else 1
        for (i in start until end step correctedDelay) {
            timeline.getOrPut(i) { mutableListOf() }.add(value)
        }
    }

    fun after(duration: Int, value: () -> Unit) {
        val start = lastTick() + 1
        val end = start + duration + 1
        for (i in start until end) {
            timeline.getOrPut(i) { mutableListOf() }.add(value)
        }
    }

    fun afterEvery(duration: Int, delay: Int, value: () -> Unit) {
        val start = lastTick() + 1
        val end = start + duration + 1
        val correctedDelay = if (delay != 0) delay else 1
        for (i in start until end step correctedDelay) {
            timeline.getOrPut(i) { mutableListOf() }.add(value)
        }
    }

    fun atEnd(value: () -> Unit) {
        timeline.getOrPut(lastTick() + 1) { mutableListOf() }.add(value)
    }

}