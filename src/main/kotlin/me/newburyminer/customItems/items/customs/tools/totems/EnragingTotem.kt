package me.newburyminer.customItems.items.customs.tools.totems

import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect
import io.papermc.paper.registry.RegistryKey
import io.papermc.paper.registry.keys.MobEffectKeys
import io.papermc.paper.registry.set.RegistrySet
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class EnragingTotem: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.ENRAGING_TOTEM

    private val material = Material.TOTEM_OF_UNDYING
    private val color = arrayOf(235, 146, 30)
    private val name = Utils.text("Enraging Totem", color)
    private val lore = Utils.loreBlockToList(
        Utils.text("Gives the same effects as a normal totem, along with Strength III (15s), Resistance II (15s) and Speed III (30s).", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name, false)
        .setLore(lore)
        .deathProtection(
            ConsumeEffect.clearAllStatusEffects(),
            ConsumeEffect.applyStatusEffects(
                listOf(PotionEffect(PotionEffectType.INSTANT_HEALTH, 1, 0), PotionEffect(PotionEffectType.ABSORPTION, 100, 2),
                    PotionEffect(PotionEffectType.REGENERATION, 900, 1), PotionEffect(PotionEffectType.FIRE_RESISTANCE, 800, 0),
                    PotionEffect(PotionEffectType.STRENGTH, 300, 2), PotionEffect(PotionEffectType.RESISTANCE, 300, 1),
                    PotionEffect(PotionEffectType.SPEED, 600, 2)), 1.0F
            )
        )
        .build()

}