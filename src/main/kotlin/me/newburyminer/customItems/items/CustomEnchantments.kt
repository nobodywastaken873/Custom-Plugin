package me.newburyminer.customItems.items

import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import io.papermc.paper.registry.TypedKey
import net.kyori.adventure.key.Key
import org.bukkit.enchantments.Enchantment

class CustomEnchantments {
    companion object {
        val AUTOSMELT: Enchantment = RegistryAccess.registryAccess()
            .getRegistry(RegistryKey.ENCHANTMENT)
            .getOrThrow(TypedKey.create(RegistryKey.ENCHANTMENT, Key.key("customitems:autosmelt")))
        val SOULBOUND: Enchantment = RegistryAccess.registryAccess()
            .getRegistry(RegistryKey.ENCHANTMENT)
            .getOrThrow(TypedKey.create(RegistryKey.ENCHANTMENT, Key.key("customitems:soulbound")))
        val FIREPROOF: Enchantment = RegistryAccess.registryAccess()
            .getRegistry(RegistryKey.ENCHANTMENT)
            .getOrThrow(TypedKey.create(RegistryKey.ENCHANTMENT, Key.key("customitems:fireproof")))
        val BLAST_RESISTANT: Enchantment = RegistryAccess.registryAccess()
            .getRegistry(RegistryKey.ENCHANTMENT)
            .getOrThrow(TypedKey.create(RegistryKey.ENCHANTMENT, Key.key("customitems:blast_resistant")))
        val REINFORCED: Enchantment = RegistryAccess.registryAccess()
            .getRegistry(RegistryKey.ENCHANTMENT)
            .getOrThrow(TypedKey.create(RegistryKey.ENCHANTMENT, Key.key("customitems:reinforced")))
        val DUPLICATE: Enchantment = RegistryAccess.registryAccess()
            .getRegistry(RegistryKey.ENCHANTMENT)
            .getOrThrow(TypedKey.create(RegistryKey.ENCHANTMENT, Key.key("customitems:duplicate")))
    }
}