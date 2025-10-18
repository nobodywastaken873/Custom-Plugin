package me.newburyminer.customItems.items.customs.tools.villagers

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.entities.CustomEntity
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import org.bukkit.Material
import org.bukkit.entity.Villager
import org.bukkit.entity.ZombieVillager
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

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is PlayerInteractEntityEvent -> {
                if (!ctx.itemType.isHand()) return
                if (e.rightClicked !is Villager && e.rightClicked !is ZombieVillager) return
                if (e.rightClicked.getTag<Int>("id") == CustomEntity.JERRY_IDOL.id) return
                e.isCancelled = true
                if (e.rightClicked is Villager) {
                    val villager: Villager = e.rightClicked as Villager
                    villager.zombify()
                } else if (e.rightClicked is ZombieVillager) {
                    val zombieVillager: ZombieVillager = e.rightClicked as ZombieVillager
                    zombieVillager.conversionTime = 50
                    zombieVillager.conversionPlayer = e.player
                }
            }

        }

    }

}