package me.newburyminer.customItems.persistent

import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType

class ItemStackTagType: PersistentDataType<ByteArray, ItemStack> {
    override fun getPrimitiveType(): Class<ByteArray> {
        return ByteArray::class.java
    }

    override fun getComplexType(): Class<ItemStack> {
        return ItemStack::class.java
    }

    override fun fromPrimitive(bytes: ByteArray, p1: PersistentDataAdapterContext): ItemStack {
        return ItemStack.deserializeBytes(bytes)
    }

    override fun toPrimitive(item: ItemStack, p1: PersistentDataAdapterContext): ByteArray {
        return item.serializeAsBytes()
    }
}