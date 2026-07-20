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

class ChorusTrim: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.CHORUS_TRIM

    private val material = Material.POPPED_CHORUS_FRUIT
    private val color = arrayOf(181, 139, 181)
    private val name = text("Chorus Fruit", color)
    private val lore = Utils.loreBlockToList(
        text("Trim material", Utils.GRAY),
    )

    private val trimMaterial = RegistryAccess.registryAccess()
        .getRegistry(RegistryKey.TRIM_MATERIAL)
        .get(Key.key("customworld", "chorus")) ?: TrimMaterial.IRON
    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name, false)
        .setLore(lore)
        .trimProvider(trimMaterial)
        .build()

}