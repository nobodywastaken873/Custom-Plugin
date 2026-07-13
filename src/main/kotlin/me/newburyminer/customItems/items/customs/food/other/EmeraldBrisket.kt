package me.newburyminer.customItems.items.customs.food.other

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

class EmeraldBrisket: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.EMERALD_BRISKET

    private val material = Material.COOKED_PORKCHOP
    private val color = arrayOf(4, 209, 32)
    private val name = text("Emerald Brisket", color)
    private val lore = Utils.loreBlockToList(
        text("Can be eaten at full hunger.", Utils.GRAY),
        text("1.6s Eat Time, 8 Hunger, 14.4 Saturation", Utils.GRAY),
        text("Potion effects: Luck X (30s), Hero of the Village X (30s)", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name, false)
        .setLore(lore)
        .food(8, 14.4F, true)
        .consumable(arrayOf(ConsumeEffect.applyStatusEffects(
            listOf(PotionEffect(PotionEffectType.LUCK, 600, 9), PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE, 600, 9)), 1.0F
        )), eatSeconds = 1.6F)
        .maxStack(64)
        .build()

}