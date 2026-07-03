package me.newburyminer.customItems.mobprovider

import me.newburyminer.customItems.Utils.Companion.removeAllAttributes
import org.bukkit.Material
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

class EquipmentBuilder {

    val equipment: MutableMap<EquipmentSlot, ItemStack> = mutableMapOf()

    fun mainhand(item: ItemStack) { equipment[EquipmentSlot.HAND] = item }
    fun mainhand(material: Material) { mainhand(ItemStack(material)) }
    fun setSlot(equipmentSlot: EquipmentSlot, item: ItemStack, resetAttributes: Boolean = true) {
        if (resetAttributes) { item.removeAllAttributes() }
        equipment[equipmentSlot] = item
    }
    fun setSlot(equipmentSlot: EquipmentSlot, material: Material, resetAttributes: Boolean = true) { setSlot(equipmentSlot, ItemStack(material), resetAttributes) }

}