package me.newburyminer.customItems.structures

data class StructureReference(
    val structure: StructureDefinition,
    val difficulty: Difficulty,
    val type: Type,
) {

    enum class Difficulty {
        NORMAL,
        OMINOUS
    }

    enum class Type {
        SPAWNER,
        VAULT
    }

    fun getColor(): Array<Int> {
        return when (difficulty) {
            Difficulty.NORMAL -> {
                when (type) {
                    Type.SPAWNER -> arrayOf(255, 146, 20)
                    Type.VAULT -> arrayOf(255, 200, 20)
                }
            }
            Difficulty.OMINOUS -> {
                when (type) {
                    Type.SPAWNER -> arrayOf(54, 114, 245)
                    Type.VAULT -> arrayOf(27, 136, 166)
                }
            }
        }
    }

}