package me.newburyminer.customItems.items.customs.food.gaps

import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class EnchantedGoldenSteak: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.ENCHANTED_GOLDEN_STEAK

    private val material = Material.COOKED_BEEF
    private val color = arrayOf(14, 224, 235)
    private val name = text("Enchanted Golden Steak", color)
    private val lore = Utils.loreBlockToList(
        text("Can be eaten at full hunger.", Utils.GRAY),
        text("1.6s Eat Time, 20 Hunger, 20 Saturation", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name, false)
        .setLore(lore)
        .food(20, 20F, true)
        .maxStack(64)
        .build()

}