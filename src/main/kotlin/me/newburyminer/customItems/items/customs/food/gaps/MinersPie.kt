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

class MinersPie: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.MINERS_PIE

    private val material = Material.PUMPKIN_PIE
    private val color = arrayOf(232, 212, 165)
    private val name = text("Miner's Pie", color)
    private val lore = Utils.loreBlockToList(
        text("Can be eaten at full hunger.", Utils.GRAY),
        text("1.7s Eat Time, 4 Hunger, 9.6 Saturation", Utils.GRAY),
        text("Potion effects: Regen II (4s), Absorption I (2m), Haste IV (20s)", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name, false)
        .setLore(lore)
        .food(4, 9.6F, true)
        .consumable(arrayOf(ConsumeEffect.applyStatusEffects(
            listOf(PotionEffect(PotionEffectType.REGENERATION, 80, 1), PotionEffect(PotionEffectType.ABSORPTION, 2400, 0),
                PotionEffect(PotionEffectType.HASTE, 400, 3)), 1.0F
        )), eatSeconds = 1.7F)
        .maxStack(64)
        .build()

}