package me.newburyminer.customItems.items.customs.food.gaps

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent
import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.getCustom
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class InstantGoldenApple: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.INSTANT_GOLDEN_APPLE

    private val material = Material.GOLDEN_APPLE
    private val color = arrayOf(235, 202, 14)
    private val name = text("Instant Golden Apple", color)
    private val lore = Utils.loreBlockToList(
        text("Can be eaten at full hunger, 15s cooldown.", Utils.GRAY),
        text("0.05s Eat Time, 4 Hunger, 9.6 Saturation", Utils.GRAY),
        text("Potion effects: Absorption I (2m)", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name, false)
        .setLore(lore)
        .food(4, 9.6F, true)
        .consumable(arrayOf(ConsumeEffect.applyStatusEffects(
            listOf(PotionEffect(PotionEffectType.ABSORPTION, 2400, 0)), 1.0F
        )), eatSeconds = 0.05F)
        .maxStack(64)
        .build()

    init {
        register(PlayerItemConsumeEvent::class, { e ->
            e.item.isItem(custom)
        },
        {e ->
            e.player.setCooldown(custom, 15.0)
        })
    }

}