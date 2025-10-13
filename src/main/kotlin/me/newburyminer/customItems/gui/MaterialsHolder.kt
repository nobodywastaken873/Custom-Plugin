package me.newburyminer.customItems.gui

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.getCustom
import me.newburyminer.customItems.Utils.Companion.name
import me.newburyminer.customItems.Utils.Companion.readableName
import me.newburyminer.customItems.systems.materials.*
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

class MaterialsHolder(private val collection: MaterialCollection, private val category: MaterialCategory): InventoryHolder {

    private val inventory: Inventory = Bukkit.createInventory(this, 27)
    init {
        MaterialType.getMaterials(category).forEach {
            val amount = collection.get(it)
            val item = getIcon(it, amount)
            inventory.addItem(item)
        }

        for (slot in inventory.contents.indices) {
            if (inventory.getItem(slot) == null)
                inventory.setItem(slot, GuiInventory.gray(true))
        }
    }


    override fun getInventory(): Inventory {
        return this.inventory
    }

    // Returns true so listener can delete the item, otherwise returns false so it does not
    fun attemptInsert(item: ItemStack): Boolean {
        // return false if it cannot be converted
        val newCollection = MaterialConverterRegistry.convert(item) ?: return false
        // if the single MaterialType does not have current category, it cannot be inserted
        val type = newCollection.singleType()
        if (category !in type.categories) return false
        if (item.getCustom() != null) return false
        val player = inventory.viewers.first() as Player
        MaterialSystem.addMaterials(player, newCollection)
        updateInventory(type, newCollection.get(type))
        collection.add(newCollection)
        return true
    }

    // Returns amount that the listener can add to the player's cursor, max 64
    fun attemptRemove(icon: ItemStack): Int {
        val type = MaterialType.getMaterials(category).first { it.icon == icon.type }
        val totalAmount = collection.get(type)
        val toRemove = totalAmount.toInt().coerceAtMost(64)
        val subtractCollection = MaterialCollection(mutableMapOf(type to toRemove.toDouble()))
        MaterialSystem.removeMaterials(inventory.viewers.first() as Player, subtractCollection)
        updateInventory(type, -toRemove.toDouble())
        collection.remove(subtractCollection)
        return toRemove
    }

    private fun updateInventory(type: MaterialType, change: Double) {
        val material = type.icon
        for (item in this.inventory) if (item.type == material) {
            val newAmount = collection.get(type) + change
            val formattedAmount = String.format("%.1f", newAmount)
            item.name(
                Utils.text(
                    "$formattedAmount ${type.readableName()}", arrayOf(245, 224, 66)
                )
            )
        }
    }

    private fun getIcon(type: MaterialType, amount: Double): ItemStack {
        val material = type.icon
        val itemStack = ItemStack(material)
        val formattedAmount = String.format("%.1f", amount)
        itemStack.name(
            Utils.text(
                "$formattedAmount ${type.readableName()}", arrayOf(245, 224, 66)
            )
        )
        return itemStack
    }
}