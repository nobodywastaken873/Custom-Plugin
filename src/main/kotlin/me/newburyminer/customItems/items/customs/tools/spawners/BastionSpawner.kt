package me.newburyminer.customItems.items.customs.tools.spawners

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.isBeingTracked
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Material
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class BastionSpawner: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.BASTION_SPAWNER

    private val material = Material.POPPED_CHORUS_FRUIT
    private val color = arrayOf(237, 223, 21)
    private val name = text("Bastion Spawner", color)
    private val lore = Utils.loreBlockToList(
        text("Right click to consume this item and begin the custom piglin boss. It will teleport players within 10 blocks of you as well. You cannot use this while being tracked, while in combat, or if someone else is fighting the boss already.", Utils.GRAY)
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    init {
        register(PlayerInteractEvent::class, { e ->
            e.item.isItem(custom) &&
            isRightClick(e) &&
            !e.player.isBeingTracked()
        },
            {e ->
                //val boss = CustomBossType.WARDEN
                //val players = e.player.location.getNearbyPlayers(20.0)

                //val spawnedBoss = BossManager.spawnBoss(boss, e.player, players.toList())
                //if (spawnedBoss) e.item?.amount -= 1
            })
    }

}