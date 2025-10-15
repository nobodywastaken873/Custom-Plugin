package me.newburyminer.customItems.persistent

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.serializeAsBytes
import org.bukkit.Location
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType

class LocationTagType: PersistentDataType<ByteArray, Location> {
    override fun getPrimitiveType(): Class<ByteArray> {
        return ByteArray::class.java
    }

    override fun getComplexType(): Class<Location> {
        return Location::class.java
    }

    override fun fromPrimitive(bytes: ByteArray, p1: PersistentDataAdapterContext): Location {
        return Utils.deserializeLocationBytes(bytes)
    }

    override fun toPrimitive(location: Location, p1: PersistentDataAdapterContext): ByteArray {
        return location.serializeAsBytes()
    }
}