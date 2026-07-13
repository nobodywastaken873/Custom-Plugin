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

class ShulkerPearl: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.SHULKER_PEARL

    private val material = Material.SHULKER_SHELL
    private val color = arrayOf(195, 187, 199)
    private val name = text("Shulker Pearl", color)
    private val lore = Utils.loreBlockToList(
        text("Can be eaten at full hunger.", Utils.GRAY),
        text("2.4s Eat Time, 0 Hunger, 0 Saturation", Utils.GRAY),
        text("Potion effects: Levitation XV (9s)", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name, false)
        .setLore(lore)
        .food(0, 0F, true)
        .consumable(arrayOf(ConsumeEffect.applyStatusEffects(
            listOf(PotionEffect(PotionEffectType.LEVITATION, 180, 14)), 1.0F
        )), eatSeconds = 2.4F)
        .maxStack(4)
        .build()

}