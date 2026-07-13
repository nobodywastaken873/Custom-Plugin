package me.newburyminer.customItems.items.customs.food.gaps

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent
import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class GoldenTurtle: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.GOLDEN_TURTLE

    private val material = Material.GOLDEN_CARROT
    private val color = arrayOf(117, 196, 6)
    private val name = text("Golden Turtle", color)
    private val lore = Utils.loreBlockToList(
        text("Can be eaten at full hunger.", Utils.GRAY),
        text("1.6s Eat Time, 4 Hunger, 7.5 Saturation", Utils.GRAY),
        text("Potion effects: Regen II (4s), Absorption I (2m), Resistance II (7s)", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name, false)
        .setLore(lore)
        .food(4, 7.5F, true)
        .consumable(arrayOf(ConsumeEffect.applyStatusEffects(
            listOf(PotionEffect(PotionEffectType.REGENERATION, 80, 1), PotionEffect(PotionEffectType.ABSORPTION, 2400, 0),
                PotionEffect(PotionEffectType.RESISTANCE, 140, 1)), 1.0F
        )), eatSeconds = 1.6F)
        .maxStack(64)
        .build()

}