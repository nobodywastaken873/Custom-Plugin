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

class PotionStew: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.POTION_STEW

    private val material = Material.BEETROOT_SOUP
    private val color = arrayOf(201, 37, 8)
    private val name = text("Potion Stew", color)
    private val lore = Utils.loreBlockToList(
        text("Can be eaten at full hunger.", Utils.GRAY),
        text("1.6s Eat Time, 4 Hunger, 5 Saturation", Utils.GRAY),
        text("Potion effects: Strength II (1m30s), Speed II (1m30s), Fire Res (8m), Invisibility (8m)", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name, false)
        .setLore(lore)
        .food(4, 5F, true)
        .consumable(arrayOf(ConsumeEffect.applyStatusEffects(
            listOf(PotionEffect(PotionEffectType.STRENGTH, 1800, 1), PotionEffect(PotionEffectType.SPEED, 1800, 1),
                PotionEffect(PotionEffectType.FIRE_RESISTANCE, 9600, 0), PotionEffect(PotionEffectType.INVISIBILITY, 9600, 0)), 1.0F
        )), eatSeconds = 1.6F)
        .maxStack(16)
        .build()

}