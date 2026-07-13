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

class PhantomCarcass: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.PHANTOM_CARCASS

    private val material = Material.PHANTOM_MEMBRANE
    private val color = arrayOf(208, 170, 227)
    private val name = text("Phantom Carcass", color)
    private val lore = Utils.loreBlockToList(
        text("1.6s Eat Time, 8 Hunger, 12.8 Saturation", Utils.GRAY),
        text("Potion effects: Invisibility (40s)", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name, false)
        .setLore(lore)
        .food(8, 12.8F)
        .consumable(arrayOf(ConsumeEffect.applyStatusEffects(
            listOf(PotionEffect(PotionEffectType.INVISIBILITY, 1200, 0)), 1.0F
        )), eatSeconds = 1.6F)
        .maxStack(64)
        .build()

}