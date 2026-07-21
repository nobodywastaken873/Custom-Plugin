package me.newburyminer.customItems.items.customs.armor.boots

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.addDoubleChestLoot
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.SimpleModifier
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class ExplorersSandals: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.EXPLORERS_SANDALS

    private val material = Material.NETHERITE_BOOTS
    private val color = arrayOf(237, 224, 140)
    private val name = text("Explorer's Sandals", color)
    private val lore = Utils.loreBlockToList(
        text("Gain permanent Speed I. Gives a +25% chance for double chest loot.", Utils.GRAY)
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .setAttributes(
            SimpleModifier(Attribute.ARMOR, 3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET),
            SimpleModifier(Attribute.ARMOR_TOUGHNESS, 3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET),
            SimpleModifier(Attribute.KNOCKBACK_RESISTANCE, 0.1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET),
        ).apply {
            this.addDoubleChestLoot(0.25)
        }
        .build()

    override val extraTasks: Map<Int, (Player) -> Unit>
        get() = mapOf(60 to {player -> runTask(player)})

    private fun runTask(player: Player) {
        if (player.inventory.boots?.isItem(custom) == true)
            player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 80, 0, false, false))
    }

}