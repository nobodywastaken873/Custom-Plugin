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

class TurtleMeat: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.TURTLE_MEAT

    private val material = Material.TURTLE_SCUTE
    private val color = arrayOf(65, 117, 49)
    private val name = text("Turtle Meat", color)
    private val lore = Utils.loreBlockToList(
        text("2.0s Eat Time, 2 Hunger, 4 Saturation", Utils.GRAY),
        text("Potion effects: Resistance IV (10s), Slowness IV (10s)", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name, false)
        .setLore(lore)
        .food(2, 4F)
        .consumable(arrayOf(ConsumeEffect.applyStatusEffects(
            listOf(PotionEffect(PotionEffectType.RESISTANCE, 200, 3), PotionEffect(PotionEffectType.SLOWNESS, 200, 3)), 1.0F
        )), eatSeconds = 2.0F)
        .maxStack(16)
        .build()

}