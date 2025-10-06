package me.newburyminer.customItems.items

import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import io.papermc.paper.registry.TypedKey
import net.kyori.adventure.key.Key

class CustomEnchantments {
    companion object {
        val AUTOSMELT = RegistryAccess.registryAccess()
            .getRegistry(RegistryKey.ENCHANTMENT)
            .getOrThrow(TypedKey.create(RegistryKey.ENCHANTMENT, Key.key("customitems:autosmelt")))
        val SOULBOUND = RegistryAccess.registryAccess()
            .getRegistry(RegistryKey.ENCHANTMENT)
            .getOrThrow(TypedKey.create(RegistryKey.ENCHANTMENT, Key.key("customitems:soulbound")))
        val FIREPROOF = RegistryAccess.registryAccess()
            .getRegistry(RegistryKey.ENCHANTMENT)
            .getOrThrow(TypedKey.create(RegistryKey.ENCHANTMENT, Key.key("customitems:fireproof")))
        val BLAST_RESISTANT = RegistryAccess.registryAccess()
            .getRegistry(RegistryKey.ENCHANTMENT)
            .getOrThrow(TypedKey.create(RegistryKey.ENCHANTMENT, Key.key("customitems:blast_resistant")))
        val REINFORCED = RegistryAccess.registryAccess()
            .getRegistry(RegistryKey.ENCHANTMENT)
            .getOrThrow(TypedKey.create(RegistryKey.ENCHANTMENT, Key.key("customitems:reinforced")))
        val DUPLICATE = RegistryAccess.registryAccess()
            .getRegistry(RegistryKey.ENCHANTMENT)
            .getOrThrow(TypedKey.create(RegistryKey.ENCHANTMENT, Key.key("customitems:duplicate")))
    }
}