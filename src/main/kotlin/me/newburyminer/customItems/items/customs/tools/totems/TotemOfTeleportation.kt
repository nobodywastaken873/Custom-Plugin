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

class TotemOfTeleportation: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.TOTEM_OF_TELEPORTATION

    private val material = Material.TOTEM_OF_UNDYING
    private val color = arrayOf(223, 190, 237)
    private val name = Utils.text("Totem of Teleportation", color)
    private val lore = Utils.loreBlockToList(
        Utils.text("Gives the same effects as a normal totem, along with teleporting you randomly in a 10 block radius.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name, false)
        .setLore(lore)
        .deathProtection(
            ConsumeEffect.clearAllStatusEffects(),
            ConsumeEffect.applyStatusEffects(
                listOf(PotionEffect(PotionEffectType.INSTANT_HEALTH, 1, 0), PotionEffect(PotionEffectType.ABSORPTION, 100, 2),
                    PotionEffect(PotionEffectType.REGENERATION, 900, 1), PotionEffect(PotionEffectType.FIRE_RESISTANCE, 800, 0)), 1.0F
            ),
            ConsumeEffect.teleportRandomlyEffect(20.0F)
        )
        .build()

}