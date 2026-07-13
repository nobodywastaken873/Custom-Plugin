package me.newburyminer.customItems.items.customs.food.other

import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class KFCBucket: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.KFC_BUCKET

    private val material = Material.COOKED_CHICKEN
    private val color = arrayOf(196, 76, 6)
    private val name = Utils.text("KFC Bucket", color)
    private val lore = Utils.loreBlockToList(
        Utils.text("Can be eaten at full hunger.", Utils.GRAY),
        Utils.text("1.4s Eat Time, 6 Hunger, 7.5 Saturation", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name, false)
        .setLore(lore)
        .food(6, 7.5F, true)
        .consumable(arrayOf(), eatSeconds = 1.4F)
        .maxStack(99)
        .build()

}