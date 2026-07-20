package me.newburyminer.customItems.items.customs.tools.totems

import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect
import io.papermc.paper.registry.RegistryKey
import io.papermc.paper.registry.keys.MobEffectKeys
import io.papermc.paper.registry.set.RegistrySet
import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.effects.AttributeData
import me.newburyminer.customItems.effects.CustomEffectType
import me.newburyminer.customItems.effects.EffectData
import me.newburyminer.customItems.effects.EffectManager
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityResurrectEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class TotemOfEscape: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.TOTEM_OF_ESCAPE

    private val material = Material.TOTEM_OF_UNDYING
    private val color = arrayOf(9, 61, 219)
    private val name = Utils.text("Totem of Escape", color)
    private val lore = Utils.loreBlockToList(
        Utils.text("Gives the same effects as a normal totem, reduces your scale by -50%, gives you Haste III (60s), Speed III (60s), and Invisibility (2m).", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name, false)
        .setLore(lore)
        .deathProtection(
            ConsumeEffect.clearAllStatusEffects(),
            ConsumeEffect.applyStatusEffects(
                listOf(PotionEffect(PotionEffectType.INSTANT_HEALTH, 1, 0), PotionEffect(PotionEffectType.ABSORPTION, 100, 2),
                    PotionEffect(PotionEffectType.REGENERATION, 900, 1), PotionEffect(PotionEffectType.FIRE_RESISTANCE, 800, 0),
                    PotionEffect(PotionEffectType.HASTE, 1200, 2), PotionEffect(PotionEffectType.SPEED, 1200, 2),
                    PotionEffect(PotionEffectType.INVISIBILITY, 1200, 0)), 1.0F
            ),
        )
        .build()

    init {
        register(EntityResurrectEvent::class, { e ->
            (e.entity.equipment?.itemInOffHand.isItem(custom) || e.entity.equipment?.itemInMainHand.isItem(custom)) &&
            !e.isCancelled
        },
        {e ->
            val player = e.entity as? Player ?: return@register
            EffectManager.applyEffect(player, CustomEffectType.ATTRIBUTE, EffectData(1200,
                AttributeData(-0.5, Attribute.SCALE, AttributeModifier.Operation.ADD_SCALAR)
            ))
        })
    }

}