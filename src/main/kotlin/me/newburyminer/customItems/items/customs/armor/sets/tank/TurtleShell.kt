package me.newburyminer.customItems.items.customs.armor.sets.tank

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.*
import me.newburyminer.customItems.items.armorsets.ArmorSet
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class TurtleShell: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.TURTLE_SHELL

    private val material = Material.NETHERITE_CHESTPLATE
    private val color = arrayOf(45, 84, 50)
    private val name = text("Turtle Shell", color)
    private val lore = Utils.loreBlockToList(
        text("Gain permanent resistance 1.", Utils.GRAY),
        text(""),
        text("Full Set Bonus (4 pieces): Tank Set", Utils.GRAY),
        text("Sneak to gain 10 absorption hearts, with a 60 second cooldown.", Utils.GRAY)
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .setAttributes(
            SimpleModifier(Attribute.ARMOR, 12.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST),
            SimpleModifier(Attribute.ARMOR_TOUGHNESS, 6.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST),
            SimpleModifier(Attribute.KNOCKBACK_RESISTANCE, 0.3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST),
            SimpleModifier(Attribute.MOVEMENT_SPEED, -0.05, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST),
            SimpleModifier(Attribute.MAX_HEALTH, 4.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST),
        )
        .setArmorSet(ArmorSet.TANK)
        .build()

    override fun handle(ctx: EventContext) {}

    override val extraTasks: Map<Int, (Player) -> Unit>
        get() = mapOf(60 to {player -> runTask(player)})

    private fun runTask(player: Player) {
        if (player.inventory.chestplate?.isItem(CustomItem.TURTLE_SHELL) == true)
            player.addPotionEffect(PotionEffect(PotionEffectType.RESISTANCE, 65, 0, false, false))
    }

}