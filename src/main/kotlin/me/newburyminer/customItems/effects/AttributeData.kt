package me.newburyminer.customItems.effects

import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import me.newburyminer.customItems.helpers.DeserializationConversion
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier

data class AttributeData(
    val amount: Double,
    val attribute: Attribute,
    val operation: AttributeModifier.Operation,
) {
    companion object: DeserializationConversion {
        fun deserialize(map: Map<String, Any>?): AttributeData? {
            if (map == null) return null
            val amount = map["amount"].asDouble()
            val keyString = map["attribute"].asString()
            val key = NamespacedKey.fromString(keyString) ?: return null
            val attribute = RegistryAccess.registryAccess().getRegistry(RegistryKey.ATTRIBUTE).get(key) ?: return null
            val operation = AttributeModifier.Operation.valueOf(map["operation"].asString())
            return AttributeData(amount, attribute, operation)
        }
    }
    fun serialize(): Map<String, Any> {
        return mapOf(
            "amount" to amount,
            "attribute" to attribute.key.asString(),
            "operation" to operation.name
        )
    }
}