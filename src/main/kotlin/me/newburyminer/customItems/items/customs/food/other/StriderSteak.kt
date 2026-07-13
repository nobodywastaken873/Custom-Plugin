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

class StriderSteak: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.STRIDER_STEAK

    private val material = Material.COOKED_BEEF
    private val color = arrayOf(173, 31, 5)
    private val name = text("Strider Steak", color)
    private val lore = Utils.loreBlockToList(
        text("1.6s Eat Time, 8 Hunger, 13.2 Saturation", Utils.GRAY),
        text("Potion effects: Fire Res (60s)", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name, false)
        .setLore(lore)
        .food(8, 13.2F)
        .consumable(arrayOf(ConsumeEffect.applyStatusEffects(
            listOf(PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1200, 0)), 1.0F
        )), eatSeconds = 1.6F)
        .maxStack(64)
        .build()

}