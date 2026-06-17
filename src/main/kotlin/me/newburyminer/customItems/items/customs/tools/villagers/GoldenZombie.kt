package me.newburyminer.customItems.items.customs.tools.villagers

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.NonPickuppableComponent
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Material
import org.bukkit.entity.Villager
import org.bukkit.entity.ZombieVillager
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.inventory.ItemStack

class GoldenZombie: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.GOLDEN_ZOMBIE

    private val material = Material.RAW_GOLD
    private val color = arrayOf(222, 205, 24)
    private val name = text("Golden Zombie", color)
    private val lore = Utils.loreBlockToList(
        text("Right click a zombie villager to cure it or a villager to zombify it.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    init {
        register(PlayerInteractAtEntityEvent::class, { e ->
            slotMatches(e, org.bukkit.inventory.EquipmentSlot.HAND, custom) &&
            (e.rightClicked is Villager || e.rightClicked is ZombieVillager) &&
            EntityWrapperManager.getWrapper(e.rightClicked.uniqueId)?.hasComponent(NonPickuppableComponent::class) != true
        },
        { e ->
            e.isCancelled = true
            if (e.rightClicked is Villager) {
                val villager: Villager = e.rightClicked as Villager
                villager.zombify()
            }
            else if (e.rightClicked is ZombieVillager) {
                val zombieVillager: ZombieVillager = e.rightClicked as ZombieVillager
                zombieVillager.conversionTime = 50
                zombieVillager.conversionPlayer = e.player
            }
        })
    }

}