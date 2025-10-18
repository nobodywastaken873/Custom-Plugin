package me.newburyminer.customItems.systems.materials

data class MaterialCollection(private val materials: MutableMap<MaterialType, Double> = mutableMapOf()) {

    companion object {
        fun deserialize(string: String): MaterialCollection {
            val newCollection = MaterialCollection()
            if (string.isEmpty()) return newCollection
            val entries = string.split(";")
            for (entry in entries) {
                val (name, amtStr) = entry.split(":")
                val type = MaterialType.valueOf(name)
                val amount = amtStr.toDoubleOrNull() ?: 0.0
                newCollection.add(type, amount)
            }
            return newCollection
        }
    }

    fun serialize(): String {
        return this.materials.entries.joinToString(";") { (type, amount) ->
            "${type.name}:${"%.3f".format(amount)}"
        }
    }

    private fun add(materialType: MaterialType, amount: Double) {
        materials[materialType] = amount + (materials[materialType] ?: 0.0)
    }

    // Returns amount removed
    private fun remove(materialType: MaterialType, amount: Double): Double {
        val current = materials[materialType] ?: 0.0
        val toRemove = if (amount > current) current else amount
        materials[materialType] = current - toRemove
        return toRemove
    }

    // Does not return a reference, updates the current one
    fun add(other: MaterialCollection) {
        for ((material, amount) in other.materials) this.add(material, amount)
    }

    // Does not return a reference, updates the current one
    fun remove(other: MaterialCollection) {
        for ((material, amount) in other.materials) this.remove(material, amount)
    }

    // Does not return a new one
    operator fun times(other: Int): MaterialCollection {
        val newCollection = MaterialCollection()
        for ((material, amount) in this.materials) newCollection.add(material, amount * other)
        return newCollection
    }

    fun get(type: MaterialType): Double {
        return materials[type] ?: 0.0
    }

    // Should always check before performing .minus()
    fun contains(other: MaterialCollection): Boolean {
        return other.materials.all {
            (key, value) ->
            this.get(key) > value
        }
    }

    fun clone(): MaterialCollection {
        val newCollection = MaterialCollection()
        for ((material, amount) in this.materials) newCollection.add(material, amount)
        return newCollection
    }

    fun getSubset(category: MaterialCategory): MaterialCollection {
        val newCollection = MaterialCollection()
        for ((type, amount) in this.materials) {
            if (category in type.categories)
                newCollection.add(type, amount)
        }
        return  newCollection
    }

    fun singleType(): MaterialType {
        return materials.entries.first().key
    }

    fun toMap(): Map<MaterialType, Double> {
        return materials.toMap()
    }
}