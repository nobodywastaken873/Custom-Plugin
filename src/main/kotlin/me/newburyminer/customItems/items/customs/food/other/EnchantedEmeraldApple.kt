package me.newburyminer.customItems.items.customs.food.other

import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect
import io.papermc.paper.registry.RegistryKey
import io.papermc.paper.registry.keys.MobEffectKeys
import io.papermc.paper.registry.set.RegistrySet
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.effects.CustomEffectType
import me.newburyminer.customItems.effects.EffectData
import me.newburyminer.customItems.effects.EffectManager
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Material
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class EnchantedEmeraldApple: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.ENCHANTED_EMERALD_APPLE

    private val material = Material.ENCHANTED_GOLDEN_APPLE
    private val color = arrayOf(75, 240, 5)
    private val name = text("Enchanted Emerald Apple", color)
    private val lore = Utils.loreBlockToList(
        text("Can be eaten at full hunger.", Utils.GRAY),
        text("1.6s Eat Time, 0 Hunger, 0 Saturation", Utils.GRAY),
        text("Gives 7 seconds of +400% double chest loot chance, meaning that you get 5x chest loot.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .food(0, 0F, true)
        .consumable(arrayOf(), eatSeconds = 1.6F)
        .maxStack(8)
        .build()

    init {
        register(PlayerItemConsumeEvent::class, { e ->
            e.item.isItem(custom)
        },
        {e ->
            EffectManager.applyEffect(e.player, CustomEffectType.QUADRUPLE_CHEST_LOOT, EffectData(20 * 7, unique = true))
        })
    }

}