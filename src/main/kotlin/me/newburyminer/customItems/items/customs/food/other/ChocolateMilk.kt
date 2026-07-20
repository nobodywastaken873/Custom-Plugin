package me.newburyminer.customItems.items.customs.food.other

import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect
import io.papermc.paper.registry.RegistryKey
import io.papermc.paper.registry.keys.MobEffectKeys
import io.papermc.paper.registry.set.RegistrySet
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class ChocolateMilk: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.CHOCOLATE_MILK

    private val material = Material.BROWN_DYE
    private val color = arrayOf(110, 79, 41)
    private val name = text("Chocolate Milk", color)
    private val lore = Utils.loreBlockToList(
        text("Can be eaten at full hunger.", Utils.GRAY),
        text("1.2s Eat Time, 0 Hunger, 0 Saturation", Utils.GRAY),
        text("Removes all negative status effects.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name, false)
        .setLore(lore)
        .food(0, 0F, true)
        .consumable(arrayOf(ConsumeEffect.removeEffects(
            RegistrySet.keySet(
                RegistryKey.MOB_EFFECT,
                MobEffectKeys.SLOWNESS,
                MobEffectKeys.MINING_FATIGUE,
                MobEffectKeys.HUNGER,
                MobEffectKeys.POISON,
                MobEffectKeys.WITHER,
                MobEffectKeys.SLOW_FALLING,
                MobEffectKeys.WEAKNESS,
                MobEffectKeys.BLINDNESS,
                MobEffectKeys.NAUSEA,
                MobEffectKeys.DARKNESS,
                MobEffectKeys.LEVITATION,
            )
        )), eatSeconds = 1.2F)
        .maxStack(32)
        .build()

}