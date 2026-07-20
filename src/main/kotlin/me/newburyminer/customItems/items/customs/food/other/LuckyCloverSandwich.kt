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

class LuckyCloverSandwich: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.LUCKY_CLOVER_SANDWICH

    private val material = Material.BREAD
    private val color = arrayOf(72, 181, 74)
    private val name = text("Lucky Clover Sandwich", color)
    private val lore = Utils.loreBlockToList(
        text("Can be eaten at full hunger.", Utils.GRAY),
        text("1.6s Eat Time, 0 Hunger, 0 Saturation", Utils.GRAY),
        text("Gives 30 minutes of +20% double chest loot chance.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name, false)
        .setLore(lore)
        .food(0, 0F, true)
        .consumable(arrayOf(), eatSeconds = 1.6F)
        .maxStack(64)
        .build()

    init {
        register(PlayerItemConsumeEvent::class, { e ->
            e.item.isItem(custom)
        },
        {e ->
            EffectManager.applyEffect(e.player, CustomEffectType.CHEST_LOOT_BUFFS, EffectData(20 * 60 * 30, unique = true))
        })
    }

}