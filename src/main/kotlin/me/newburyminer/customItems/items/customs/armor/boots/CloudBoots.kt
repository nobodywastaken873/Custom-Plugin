package me.newburyminer.customItems.items.customs.armor.boots

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

class CloudBoots: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.CLOUD_BOOTS

    private val material = Material.NETHERITE_BOOTS
    private val color = arrayOf(193, 216, 227)
    private val name = text("Cloud Boots", color)
    private val lore = mutableListOf<Component>()

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .setAttributes(
            SimpleModifier(Attribute.ARMOR, 5.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET),
            SimpleModifier(Attribute.ARMOR_TOUGHNESS, 4.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET),
            SimpleModifier(Attribute.KNOCKBACK_RESISTANCE, 0.1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET),
            SimpleModifier(Attribute.FALL_DAMAGE_MULTIPLIER, -1.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET),
            SimpleModifier(Attribute.MAX_HEALTH, 4.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET),
            SimpleModifier(Attribute.MOVEMENT_EFFICIENCY, 1.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET),
            SimpleModifier(Attribute.STEP_HEIGHT, 0.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET),
        )
        .build()

    override fun handle(ctx: EventContext) {}

    override val extraTasks: Map<Int, (Player) -> Unit>
        get() = mapOf(60 to {player -> runTask(player)})

    private fun runTask(player: Player) {
        if (player.inventory.boots?.isItem(CustomItem.CLOUD_BOOTS) == true)
            player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 65, 1, false, false))
    }

}