package me.newburyminer.customItems.mobprovider

import org.bukkit.Material
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

class EquipmentBuilder {

    val equipment: MutableMap<EquipmentSlot, ItemStack> = mutableMapOf()

    fun mainhand(item: ItemStack) { equipment[EquipmentSlot.HAND] = item }
    fun mainhand(material: Material) { mainhand(ItemStack(material)) }
    fun setSlot(equipmentSlot: EquipmentSlot, item: ItemStack) { equipment[equipmentSlot] = item }
    fun setSlot(equipmentSlot: EquipmentSlot, material: Material) { setSlot(equipmentSlot, ItemStack(material)) }

}