package me.newburyminer.customItems.helpers

import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import io.papermc.paper.registry.TypedKey
import net.kyori.adventure.key.Key

class CustomDamageType {
    companion object {
        val SHIELD_BLOCKED = RegistryAccess.registryAccess()
            .getRegistry(RegistryKey.DAMAGE_TYPE)
            .getOrThrow(TypedKey.create(RegistryKey.DAMAGE_TYPE, Key.key("minecraft:shield_no_scale")))
        val ARMOR_BYPASS = RegistryAccess.registryAccess()
            .getRegistry(RegistryKey.DAMAGE_TYPE)
            .getOrThrow(TypedKey.create(RegistryKey.DAMAGE_TYPE, Key.key("minecraft:armor_bypass_no_scale")))
        val ALL_BYPASS = RegistryAccess.registryAccess()
            .getRegistry(RegistryKey.DAMAGE_TYPE)
            .getOrThrow(TypedKey.create(RegistryKey.DAMAGE_TYPE, Key.key("minecraft:bypass_no_scale")))
        val HOT_FLOOR = RegistryAccess.registryAccess()
            .getRegistry(RegistryKey.DAMAGE_TYPE)
            .getOrThrow(TypedKey.create(RegistryKey.DAMAGE_TYPE, Key.key("minecraft:lava_no_scale")))
        val DEFAULT = RegistryAccess.registryAccess()
            .getRegistry(RegistryKey.DAMAGE_TYPE)
            .getOrThrow(TypedKey.create(RegistryKey.DAMAGE_TYPE, Key.key("minecraft:default_no_scale")))
    }
}