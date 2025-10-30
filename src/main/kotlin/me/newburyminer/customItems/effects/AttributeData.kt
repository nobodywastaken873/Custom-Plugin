package me.newburyminer.customItems.effects

import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import java.rmi.registry.Registry

class AttributeData(
    val amount: Double,
    val attribute: Attribute,
    val operation: AttributeModifier.Operation,
) {
    companion object {
        fun deserialize(map: Map<String, Any>?): AttributeData? {
            if (map == null) return null
            val amount = map["amount"] as Double
            val keyString = map["attribute"] as String
            val key = NamespacedKey.fromString(keyString) ?: return null
            val attribute = RegistryAccess.registryAccess().getRegistry(RegistryKey.ATTRIBUTE).get(key) ?: return null
            val operation = AttributeModifier.Operation.valueOf(map["operation"] as String)
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