package me.newburyminer.customItems.items.customs.armor.helmets

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.isInCombat
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

class EdibleHelmet: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.EDIBLE_HELMET

    private val material = Material.NETHERITE_HELMET
    private val color = arrayOf(64, 189, 70)
    private val name = text("Edible Helmet", color)
    private val lore = Utils.loreBlockToList(
        text("Gain 1 saturation or hunger every 5 seconds when not in combat.", Utils.GRAY)
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .setAttributes(
            SimpleModifier(Attribute.ARMOR, 3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD),
            SimpleModifier(Attribute.ARMOR_TOUGHNESS, 3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD),
            SimpleModifier(Attribute.MAX_HEALTH, 4.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD)
        )
        .build()

    override val extraTasks: Map<Int, (Player) -> Unit>
        get() = mapOf(100 to {player -> runTask(player)})

    private fun runTask(player: Player) {
        if (player.inventory.helmet?.isItem(custom) != true) return
        if (player.isInCombat()) return

        if (player.foodLevel != 20) {
            player.foodLevel += 1
        }
        else if (player.saturation < 20.0) {
            player.saturation = (player.saturation + 1.0).coerceAtMost(20.0).toFloat()
        }
    }

}