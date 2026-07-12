package me.newburyminer.customItems.structures.locations

data class ChunkPos(
    val x: Int,
    val z: Int,
) {
    val key: Long
        get() = (x.toLong() shl 32) or (z.toLong() and 0xffffffffL)
}
