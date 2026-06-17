package me.newburyminer.customItems.helpers

import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import io.papermc.paper.registry.TypedKey
import net.kyori.adventure.key.Key
import org.bukkit.damage.DamageType

class CustomDamageType {
    companion object {
        val ALL_BYPASS_NO_CD = RegistryAccess.registryAccess()
            .getRegistry(RegistryKey.DAMAGE_TYPE)
            .getOrThrow(TypedKey.create(RegistryKey.DAMAGE_TYPE, Key.key("customworld:all_bypass_nocd")))
        val ALL_BYPASS = RegistryAccess.registryAccess()
            .getRegistry(RegistryKey.DAMAGE_TYPE)
            .getOrThrow(TypedKey.create(RegistryKey.DAMAGE_TYPE, Key.key("customworld:all_bypass")))
        val BURNING_NO_CD = RegistryAccess.registryAccess()
            .getRegistry(RegistryKey.DAMAGE_TYPE)
            .getOrThrow(TypedKey.create(RegistryKey.DAMAGE_TYPE, Key.key("customworld:burning_nocd")))
        val BURNING = RegistryAccess.registryAccess()
            .getRegistry(RegistryKey.DAMAGE_TYPE)
            .getOrThrow(TypedKey.create(RegistryKey.DAMAGE_TYPE, Key.key("customworld:burning")))
        val DEFAULT_NO_CD = RegistryAccess.registryAccess()
            .getRegistry(RegistryKey.DAMAGE_TYPE)
            .getOrThrow(TypedKey.create(RegistryKey.DAMAGE_TYPE, Key.key("customworld:default_nocd")))
        val DEFAULT = RegistryAccess.registryAccess()
            .getRegistry(RegistryKey.DAMAGE_TYPE)
            .getOrThrow(TypedKey.create(RegistryKey.DAMAGE_TYPE, Key.key("customworld:default")))
        val ENCH_BYPASS_NO_CD = RegistryAccess.registryAccess()
            .getRegistry(RegistryKey.DAMAGE_TYPE)
            .getOrThrow(TypedKey.create(RegistryKey.DAMAGE_TYPE, Key.key("customworld:ench_bypass_nocd")))
        val ENCH_BYPASS = RegistryAccess.registryAccess()
            .getRegistry(RegistryKey.DAMAGE_TYPE)
            .getOrThrow(TypedKey.create(RegistryKey.DAMAGE_TYPE, Key.key("customworld:ench_bypass")))
        val EXPLOSION_NO_CD = RegistryAccess.registryAccess()
            .getRegistry(RegistryKey.DAMAGE_TYPE)
            .getOrThrow(TypedKey.create(RegistryKey.DAMAGE_TYPE, Key.key("customworld:explosion_nocd")))
        val EXPLOSION = RegistryAccess.registryAccess()
            .getRegistry(RegistryKey.DAMAGE_TYPE)
            .getOrThrow(TypedKey.create(RegistryKey.DAMAGE_TYPE, Key.key("customworld:explosion")))
        val MAGIC_NO_CD = RegistryAccess.registryAccess()
            .getRegistry(RegistryKey.DAMAGE_TYPE)
            .getOrThrow(TypedKey.create(RegistryKey.DAMAGE_TYPE, Key.key("customworld:magic_nocd")))
        val MAGIC = RegistryAccess.registryAccess()
            .getRegistry(RegistryKey.DAMAGE_TYPE)
            .getOrThrow(TypedKey.create(RegistryKey.DAMAGE_TYPE, Key.key("customworld:magic")))
        val MELEE_NO_CD = RegistryAccess.registryAccess()
            .getRegistry(RegistryKey.DAMAGE_TYPE)
            .getOrThrow(TypedKey.create(RegistryKey.DAMAGE_TYPE, Key.key("customworld:melee_nocd")))
        val MELEE = RegistryAccess.registryAccess()
            .getRegistry(RegistryKey.DAMAGE_TYPE)
            .getOrThrow(TypedKey.create(RegistryKey.DAMAGE_TYPE, Key.key("customworld:melee")))
        val PROJECTILE_NO_CD = RegistryAccess.registryAccess()
            .getRegistry(RegistryKey.DAMAGE_TYPE)
            .getOrThrow(TypedKey.create(RegistryKey.DAMAGE_TYPE, Key.key("customworld:projectile_nocd")))
        val PROJECTILE = RegistryAccess.registryAccess()
            .getRegistry(RegistryKey.DAMAGE_TYPE)
            .getOrThrow(TypedKey.create(RegistryKey.DAMAGE_TYPE, Key.key("customworld:projectile")))

        fun DamageType.isCustom(): Boolean {
            return this.key.namespace == "customworld"
        }
    }
}