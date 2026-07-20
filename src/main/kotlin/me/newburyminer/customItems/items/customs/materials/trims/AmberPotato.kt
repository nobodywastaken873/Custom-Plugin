package me.newburyminer.customItems.items.customs.materials.trims

import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import net.kyori.adventure.key.Key
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.trim.TrimMaterial

class AmberPotato: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.AMBER_POTATO

    private val material = Material.POISONOUS_POTATO
    private val color = arrayOf(229, 137, 4)
    private val name = text("Amber Potato", color)
    private val lore = Utils.loreBlockToList(
        text("Trim material", Utils.GRAY),
    )

    private val trimMaterial = RegistryAccess.registryAccess()
        .getRegistry(RegistryKey.TRIM_MATERIAL)
        .get(Key.key("customworld", "amberpotato")) ?: TrimMaterial.IRON
    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name, false)
        .setLore(lore)
        .trimProvider(trimMaterial)
        .build()

}