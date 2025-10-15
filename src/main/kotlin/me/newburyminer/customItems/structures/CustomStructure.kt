package me.newburyminer.customItems.structures

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.helpers.RandomSelector
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class StructureLoot(private val normalSpawner: CustomLootTable, private val ominousSpawner: CustomLootTable,
                    private val normalVault: CustomLootTable, private val ominousVault: CustomLootTable) {
    fun get(type: String, difficulty: String): CustomLootTable {
        return if (type == "spawner") {
            if (difficulty == "normal") {
                normalSpawner
            } else {
                ominousSpawner
            }
        } else {
            if (difficulty == "normal") {
                normalVault
            } else {
                ominousVault
            }
        }
    }
}

enum class CustomStructure(var text: String = "", var tag: String = "") {

    ABANDONED_SHIP,
    AGED_OCEAN_MONUMENT,
    BADLANDS_OUTPOST,
    BLACK_TOWERS,
    DEEP_BLUE_CITY,
    DESERT_COURTYARD,
    DESERT_MOSQUE,
    DESERT_RUINS,
    DESERT_TEMPLE,
    DETERIORATED_BRIDGE,
    DROWNED_OUTPOST,
    JUNGLE_TEMPLE,
    LITTLE_MUD_FORT,
    MANGROVE_HOUSE,
    MARBLE_FORT,
    MASSIVE_DESERT_TEMPLE,
    MASSIVE_OCEAN_MONUMENT,
    OCEAN_QUARRY,
    OCEAN_RUINS,
    PURPLE_TOWERS,
    STONE_FORT,
    TENT_CAMP,


    ;

    lateinit var loot: StructureLoot

    init {
        var newName = ""
        for (word in this.name.split("_")) {
            var newWord = word.lowercase().capitalize()
            newName += "$newWord "
        }
        newName = newName.substring(0, newName.lastIndex)
        this.text = newName
        this.tag = this.name.lowercase()
    }

    fun key(difficulty: String, text: String = this.text, tag: String = this.tag): ItemStack {
        val item: ItemStack
        if (difficulty == "normal") {
            item = ItemStack(Material.TRIAL_KEY)
            val newMeta = item.itemMeta
            val newComponent = newMeta.customModelDataComponent
            newComponent.strings = listOf("${tag}_normal_key")
            newMeta.setCustomModelDataComponent(newComponent)
            newMeta.customName(Utils.text("$text Trial Key").style(Style.style(NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true)))
            item.itemMeta = newMeta
        } else {
            item = ItemStack(Material.OMINOUS_TRIAL_KEY)
            val newMeta = item.itemMeta
            val newComponent = newMeta.customModelDataComponent
            newComponent.strings = listOf("${tag}_ominous_key")
            newMeta.setCustomModelDataComponent(newComponent)
            newMeta.customName(Utils.text("$text Ominous Trial Key").style(Style.style(NamedTextColor.DARK_AQUA).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true)))
            item.itemMeta = newMeta
        }
        return item
    }

    companion object {
        init {
            ABANDONED_SHIP.loot = StructureLoot(
                // Normal Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(ABANDONED_SHIP.key("normal"), 1)
                    ), 1..1
                ),
                // Ominous Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(ABANDONED_SHIP.key("ominous"), 1)
                    ), 1..1
                ),
                // Normal Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.IRON_INGOT), 1)
                    ), 1..1
                ),
                // Ominous Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.DIAMOND), 1)
                    ), 1..1
                ),
            )
            AGED_OCEAN_MONUMENT.loot = StructureLoot(
                // Normal Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(AGED_OCEAN_MONUMENT.key("normal"), 1)
                    ), 1..1
                ),
                // Ominous Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(AGED_OCEAN_MONUMENT.key("ominous"), 1)
                    ), 1..1
                ),
                // Normal Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.IRON_INGOT), 1)
                    ), 1..1
                ),
                // Ominous Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.DIAMOND), 1)
                    ), 1..1
                ),
            )
            BADLANDS_OUTPOST.loot = StructureLoot(
                // Normal Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(BADLANDS_OUTPOST.key("normal"), 1)
                    ), 1..1
                ),
                // Ominous Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(BADLANDS_OUTPOST.key("ominous"), 1)
                    ), 1..1
                ),
                // Normal Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.IRON_INGOT), 1)
                    ), 1..1
                ),
                // Ominous Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.DIAMOND), 1)
                    ), 1..1
                ),
            )
            BLACK_TOWERS.loot = StructureLoot(
                // Normal Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(BLACK_TOWERS.key("normal"), 1)
                    ), 1..1
                ),
                // Ominous Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(BLACK_TOWERS.key("ominous"), 1)
                    ), 1..1
                ),
                // Normal Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.IRON_INGOT), 1)
                    ), 1..1
                ),
                // Ominous Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.DIAMOND), 1)
                    ), 1..1
                ),
            )
            DEEP_BLUE_CITY.loot = StructureLoot(
                // Normal Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(DEEP_BLUE_CITY.key("normal"), 1)
                    ), 1..1
                ),
                // Ominous Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(DEEP_BLUE_CITY.key("ominous"), 1)
                    ), 1..1
                ),
                // Normal Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.IRON_INGOT), 1)
                    ), 1..1
                ),
                // Ominous Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.DIAMOND), 1)
                    ), 1..1
                ),
            )
            DESERT_COURTYARD.loot = StructureLoot(
                // Normal Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(DESERT_COURTYARD.key("normal"), 1)
                    ), 1..1
                ),
                // Ominous Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(DESERT_COURTYARD.key("ominous"), 1)
                    ), 1..1
                ),
                // Normal Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.IRON_INGOT), 1)
                    ), 1..1
                ),
                // Ominous Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.DIAMOND), 1)
                    ), 1..1
                ),
            )
            DESERT_MOSQUE.loot = StructureLoot(
                // Normal Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(DESERT_MOSQUE.key("normal"), 1)
                    ), 1..1
                ),
                // Ominous Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(DESERT_MOSQUE.key("ominous"), 1)
                    ), 1..1
                ),
                // Normal Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.IRON_INGOT), 1)
                    ), 1..1
                ),
                // Ominous Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.DIAMOND), 1)
                    ), 1..1
                ),
            )
            DESERT_RUINS.loot = StructureLoot(
                // Normal Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(DESERT_RUINS.key("normal"), 1)
                    ), 1..1
                ),
                // Ominous Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(DESERT_RUINS.key("ominous"), 1)
                    ), 1..1
                ),
                // Normal Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.IRON_INGOT), 1)
                    ), 1..1
                ),
                // Ominous Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.DIAMOND), 1)
                    ), 1..1
                ),
            )
            DESERT_TEMPLE.loot = StructureLoot(
                // Normal Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(DESERT_TEMPLE.key("normal"), 1)
                    ), 1..1
                ),
                // Ominous Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(DESERT_TEMPLE.key("ominous"), 1)
                    ), 1..1
                ),
                // Normal Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.IRON_INGOT), 1)
                    ), 1..1
                ),
                // Ominous Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.DIAMOND), 1)
                    ), 1..1
                ),
            )
            DETERIORATED_BRIDGE.loot = StructureLoot(
                // Normal Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(DETERIORATED_BRIDGE.key("normal"), 1)
                    ), 1..1
                ),
                // Ominous Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(DETERIORATED_BRIDGE.key("ominous"), 1)
                    ), 1..1
                ),
                // Normal Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.IRON_INGOT), 1)
                    ), 1..1
                ),
                // Ominous Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.DIAMOND), 1)
                    ), 1..1
                ),
            )
            DROWNED_OUTPOST.loot = StructureLoot(
                // Normal Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(DROWNED_OUTPOST.key("normal"), 1)
                    ), 1..1
                ),
                // Ominous Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(DROWNED_OUTPOST.key("ominous"), 1)
                    ), 1..1
                ),
                // Normal Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.IRON_INGOT), 1)
                    ), 1..1
                ),
                // Ominous Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.DIAMOND), 1)
                    ), 1..1
                ),
            )
            JUNGLE_TEMPLE.loot = StructureLoot(
                // Normal Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(JUNGLE_TEMPLE.key("normal"), 1)
                    ), 1..1
                ),
                // Ominous Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(JUNGLE_TEMPLE.key("ominous"), 1)
                    ), 1..1
                ),
                // Normal Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.IRON_INGOT), 1)
                    ), 1..1
                ),
                // Ominous Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.DIAMOND), 1)
                    ), 1..1
                ),
            )
            LITTLE_MUD_FORT.loot = StructureLoot(
                // Normal Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(LITTLE_MUD_FORT.key("normal"), 1)
                    ), 1..1
                ),
                // Ominous Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(LITTLE_MUD_FORT.key("ominous"), 1)
                    ), 1..1
                ),
                // Normal Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.IRON_INGOT), 1)
                    ), 1..1
                ),
                // Ominous Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.DIAMOND), 1)
                    ), 1..1
                ),
            )
            MANGROVE_HOUSE.loot = StructureLoot(
                // Normal Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(MANGROVE_HOUSE.key("normal"), 1)
                    ), 1..1
                ),
                // Ominous Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(MANGROVE_HOUSE.key("ominous"), 1)
                    ), 1..1
                ),
                // Normal Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.IRON_INGOT), 1)
                    ), 1..1
                ),
                // Ominous Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.DIAMOND), 1)
                    ), 1..1
                ),
            )
            MARBLE_FORT.loot = StructureLoot(
                // Normal Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(MARBLE_FORT.key("normal"), 1)
                    ), 1..1
                ),
                // Ominous Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(MARBLE_FORT.key("ominous"), 1)
                    ), 1..1
                ),
                // Normal Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.IRON_INGOT), 1)
                    ), 1..1
                ),
                // Ominous Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.DIAMOND), 1)
                    ), 1..1
                ),
            )
            MASSIVE_DESERT_TEMPLE.loot = StructureLoot(
                // Normal Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(MASSIVE_DESERT_TEMPLE.key("normal"), 1)
                    ), 1..1
                ),
                // Ominous Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(MASSIVE_DESERT_TEMPLE.key("ominous"), 1)
                    ), 1..1
                ),
                // Normal Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.IRON_INGOT), 1)
                    ), 1..1
                ),
                // Ominous Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.DIAMOND), 1)
                    ), 1..1
                ),
            )
            MASSIVE_OCEAN_MONUMENT.loot = StructureLoot(
                // Normal Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(MASSIVE_OCEAN_MONUMENT.key("normal"), 1)
                    ), 1..1
                ),
                // Ominous Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(MASSIVE_OCEAN_MONUMENT.key("ominous"), 1)
                    ), 1..1
                ),
                // Normal Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.IRON_INGOT), 1)
                    ), 1..1
                ),
                // Ominous Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.DIAMOND), 1)
                    ), 1..1
                ),
            )
            OCEAN_QUARRY.loot = StructureLoot(
                // Normal Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(OCEAN_QUARRY.key("normal"), 1)
                    ), 1..1
                ),
                // Ominous Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(OCEAN_QUARRY.key("ominous"), 1)
                    ), 1..1
                ),
                // Normal Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.IRON_INGOT), 1)
                    ), 1..1
                ),
                // Ominous Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.DIAMOND), 1)
                    ), 1..1
                ),
            )
            OCEAN_RUINS.loot = StructureLoot(
                // Normal Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(OCEAN_RUINS.key("normal"), 1)
                    ), 1..1
                ),
                // Ominous Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(OCEAN_RUINS.key("ominous"), 1)
                    ), 1..1
                ),
                // Normal Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.IRON_INGOT), 1)
                    ), 1..1
                ),
                // Ominous Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.DIAMOND), 1)
                    ), 1..1
                ),
            )
            PURPLE_TOWERS.loot = StructureLoot(
                // Normal Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(PURPLE_TOWERS.key("normal"), 1)
                    ), 1..1
                ),
                // Ominous Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(PURPLE_TOWERS.key("ominous"), 1)
                    ), 1..1
                ),
                // Normal Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.IRON_INGOT), 1)
                    ), 1..1
                ),
                // Ominous Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.DIAMOND), 1)
                    ), 1..1
                ),
            )
            STONE_FORT.loot = StructureLoot(
                // Normal Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(STONE_FORT.key("normal"), 1)
                    ), 1..1
                ),
                // Ominous Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(STONE_FORT.key("ominous"), 1)
                    ), 1..1
                ),
                // Normal Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.IRON_INGOT), 1)
                    ), 1..1
                ),
                // Ominous Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.DIAMOND), 1)
                    ), 1..1
                ),
            )
            TENT_CAMP.loot = StructureLoot(
                // Normal Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(TENT_CAMP.key("normal"), 1)
                    ), 1..1
                ),
                // Ominous Spawner
                CustomLootTable(
                    RandomSelector(
                        Pair(TENT_CAMP.key("ominous"), 1)
                    ), 1..1
                ),
                // Normal Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.IRON_INGOT), 1)
                    ), 1..1
                ),
                // Ominous Vault
                CustomLootTable(
                    RandomSelector(
                        Pair(ItemStack(Material.DIAMOND), 1)
                    ), 1..1
                ),
            )
        }

        fun get(tag: String): CustomStructure {
            for (structure in CustomStructure.entries) {
                if (structure.tag == tag) {
                    return structure
                }
            }
            return CustomStructure.entries.first()
        }
    }
}