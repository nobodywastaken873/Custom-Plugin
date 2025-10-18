package me.newburyminer.customItems.items.customs.armor.chestplates

import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.*
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class MoltenChestplate: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.MOLTEN_CHESTPLATE

    private val material = Material.NETHERITE_CHESTPLATE
    private val color = arrayOf(209, 103, 4)
    private val name = text("Molten Chestplate", color)
    private val lore = mutableListOf<Component>()

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .setAttributes(
            SimpleModifier(Attribute.ARMOR, 8.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST),
            SimpleModifier(Attribute.ARMOR_TOUGHNESS, 4.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST),
            SimpleModifier(Attribute.KNOCKBACK_RESISTANCE, 0.1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST),
            SimpleModifier(Attribute.BURNING_TIME, -1.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST),
            SimpleModifier(Attribute.MAX_HEALTH, 4.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST),
        )
        .build()

    override fun handle(ctx: EventContext) {}

    override val extraTasks: Map<Int, (Player) -> Unit>
        get() = mapOf(60 to {player -> runTask(player)})

    private fun runTask(player: Player) {
        if (player.inventory.chestplate?.isItem(CustomItem.MOLTEN_CHESTPLATE) == true)
            player.addPotionEffect(PotionEffect(PotionEffectType.FIRE_RESISTANCE, 65, 0, false, false))
    }

}