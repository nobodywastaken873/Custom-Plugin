package me.newburyminer.customItems.mobprovider

import io.papermc.paper.datacomponent.DataComponentType
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.DyedItemColor
import me.newburyminer.customItems.Utils.Companion.removeAllAttributes
import me.newburyminer.customItems.Utils.Companion.trim
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern

class EquipmentBuilder {

    val equipment: MutableMap<EquipmentSlot, ItemStack> = mutableMapOf()

    fun mainhand(item: ItemStack) { equipment[EquipmentSlot.HAND] = item }
    fun mainhand(material: Material) { mainhand(ItemStack(material)) }
    fun setSlot(equipmentSlot: EquipmentSlot, item: ItemStack, resetAttributes: Boolean = true) {
        if (resetAttributes) { item.removeAllAttributes() }
        equipment[equipmentSlot] = item
    }
    fun setSlot(equipmentSlot: EquipmentSlot, material: Material, resetAttributes: Boolean = true) { setSlot(equipmentSlot, ItemStack(material), resetAttributes) }

    fun offhand(material: Material, resetAttributes: Boolean = true) {setSlot(EquipmentSlot.OFF_HAND, material, resetAttributes) }
    fun offhand(item: ItemStack, resetAttributes: Boolean = true) {setSlot(EquipmentSlot.OFF_HAND, item, resetAttributes) }
    fun boots(material: Material, trim: ArmorTrim, resetAttributes: Boolean = true) {
        setSlot(EquipmentSlot.FEET, ItemStack(material).trim(trim), resetAttributes)
    }
    fun legs(material: Material, trim: ArmorTrim, resetAttributes: Boolean = true) {
        setSlot(EquipmentSlot.LEGS, ItemStack(material).trim(trim), resetAttributes)
    }
    fun chest(material: Material, trim: ArmorTrim, resetAttributes: Boolean = true) {
        setSlot(EquipmentSlot.CHEST, ItemStack(material).trim(trim), resetAttributes)
    }
    fun helm(material: Material, trim: ArmorTrim, resetAttributes: Boolean = true) {
        setSlot(EquipmentSlot.HEAD, ItemStack(material).trim(trim), resetAttributes)
    }

    fun leatherLegs(color: Array<Int>, trim: ArmorTrim, resetAttributes: Boolean = true) {
        val legs = ItemStack(Material.LEATHER_LEGGINGS).trim(trim)
        legs.setData(DataComponentTypes.DYED_COLOR, DyedItemColor.dyedItemColor(Color.fromRGB(color[0], color[1], color[2])))
        setSlot(EquipmentSlot.LEGS, legs, resetAttributes)
    }
    fun leatherChest(color: Array<Int>, trim: ArmorTrim, resetAttributes: Boolean = true) {
        val legs = ItemStack(Material.LEATHER_CHESTPLATE).trim(trim)
        legs.setData(DataComponentTypes.DYED_COLOR, DyedItemColor.dyedItemColor(Color.fromRGB(color[0], color[1], color[2])))
        setSlot(EquipmentSlot.CHEST, legs, resetAttributes)
    }
    fun setArmor(color: Array<Int>, trim: ArmorTrim) {
        leatherLegs(color, trim)
        leatherChest(color, trim)
    }

}