package me.newburyminer.customItems.items.customs.tools.mining

import io.papermc.paper.registry.keys.tags.BlockTypeTagKeys
import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.loreBlock
import me.newburyminer.customItems.Utils.Companion.name
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.SimpleModifier
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

class NetheriteMattock: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.NETHERITE_MATTOCK

    private val material = Material.NETHERITE_PICKAXE
    private val color = arrayOf(54, 35, 64)
    private val name = text("Netherite Mattock", color)
    private val lore = Utils.loreBlockToList(
        text("Works as a axe, pickaxe, shovel, and hoe when breaking blocks. Right click whilst sneaking to toggle between silk touch and fortune.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setAttributes(
            SimpleModifier(Attribute.ATTACK_DAMAGE, 9.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
            SimpleModifier(Attribute.ATTACK_SPEED, -3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
        )
        .setLore(lore)
        .tool(1, 1F,
            BlockTypeTagKeys.MINEABLE_HOE to 9F,
            BlockTypeTagKeys.MINEABLE_SHOVEL to 9F,
            BlockTypeTagKeys.MINEABLE_AXE to 9F,
            BlockTypeTagKeys.MINEABLE_PICKAXE to 9F)
        .build()

    init {
        register(PlayerInteractEvent::class, { e ->
            e.item.isItem(custom) &&
            e.player.isSneaking &&
            e.player.offCooldown(custom) &&
            isRightClick(e)
        },
        {e ->

            val pick = e.item ?: return@register

            if (pick.getEnchantmentLevel(Enchantment.FORTUNE) > 3) {
                pick.setTag("overmaxfortune", pick.getEnchantmentLevel(Enchantment.FORTUNE))
            }

            if (pick.getEnchantmentLevel(Enchantment.FORTUNE) > 0) {
                pick.removeEnchantment(Enchantment.FORTUNE)
                pick.addEnchantment(Enchantment.SILK_TOUCH, 1)
                pick.name(text("Netherite Mattock - Silk Touch", color))
            }

            else if (pick.getEnchantmentLevel(Enchantment.SILK_TOUCH) > 0) {
                pick.removeEnchantment(Enchantment.SILK_TOUCH)
                val fortuneLevel = pick.getTag<Int>("overmaxfortune") ?: 3
                pick.addEnchantment(Enchantment.FORTUNE, fortuneLevel)
                pick.name(text("Netherite Mattock - Fortune", color))
            }

            CustomEffects.playSoundToPlayer(e.player, Sound.UI_BUTTON_CLICK, 1.0F, 1.1F)
            pick.loreBlock(
                text("Works as a axe, pickaxe, shovel, and hoe when breaking blocks. Right click whilst sneaking to toggle between silk touch and fortune.", Utils.GRAY)
            )
            e.player.setCooldown(custom, 0.5)
        })
    }

}