package me.newburyminer.customItems.systems

import com.destroystokyo.paper.event.player.PlayerJumpEvent
import me.newburyminer.customItems.Utils.Companion.getDoubleChestLoot
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.isAfk
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.effects.AttributeData
import me.newburyminer.customItems.effects.CustomEffectType
import me.newburyminer.customItems.effects.EffectData
import me.newburyminer.customItems.effects.EffectManager
import me.newburyminer.customItems.eventbus.EventRegistry
import me.newburyminer.customItems.eventbus.ListenerEntry
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.world.LootGenerateEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.loot.LootContext
import org.bukkit.scheduler.BukkitRunnable
import java.util.UUID

object SurvivalTimeSystem: BukkitRunnable() {

    private val actionTimestamp = mutableMapOf<UUID, Int>()

    private const val MAX_BUFF_SECONDS = 60 * 60 * 12
    private const val MIN_BUFF_SECONDS = 60 * 60 * 4

    fun registerEvents() {

        registerActionListeners()

        EventRegistry.register(ListenerEntry(PlayerDeathEvent::class,
            { e ->
                !e.isCancelled
            },
            {e ->
                resetSurvived(e.player)
            })
        )

        EventRegistry.register(ListenerEntry(LootGenerateEvent::class,
            { e ->
                !e.isCancelled &&
                e.entity is Player
            },
            {e ->
                val player = e.entity as Player
                val doubleChance =
                    getBuffFraction(player.getTag<Int>("survivaltime") ?: 0) * 0.4 +
                    if (EffectManager.hasEffect(player, CustomEffectType.CHEST_LOOT_BUFFS)) 0.25 else 0.0 +
                    if (EffectManager.hasEffect(player, CustomEffectType.QUADRUPLE_CHEST_LOOT)) 4.0 else 0.0 +
                    player.equipment.armorContents.filterNotNull().sumOf { it.getDoubleChestLoot() }

                val extraChests = doubleChance.toInt() + if (Math.random() < doubleChance % 1.0) 1 else 0

                var chestLoot = e.loot.toList()
                repeat(extraChests) {
                    chestLoot = chestLoot + e.lootTable.populateLoot(null,
                        LootContext.Builder(Location(Bukkit.getWorlds()[0], Math.random() * 1000, 0.0, Math.random() * 1000))
                            .killer(player).build())
                }

                if (chestLoot.size > 27) {

                    val newLoot = mutableListOf<ItemStack>()
                    val iterator = chestLoot.toList()

                    for (item in iterator) {
                        if (newLoot.any { item.isSimilar(it) }) {
                            continue
                        }
                        val count = iterator.filter { item.isSimilar(it) }.sumOf { it.amount }

                        val stackCount = count / item.maxStackSize
                        val extra = count % item.maxStackSize

                        repeat(stackCount) {
                            val newItem = item.clone()
                            newItem.amount = item.maxStackSize
                            newLoot.add(newItem)
                        }

                        if (extra > 0) {
                            val newItem = item.clone()
                            newItem.amount = extra
                            newLoot.add(newItem)
                        }
                    }

                    chestLoot =
                        if (newLoot.size > 27) newLoot.shuffled().take(27)
                        else newLoot.toList()

                }

                e.setLoot(chestLoot)
            })
        )

    }
    private fun registerActionListeners() {

        EventRegistry.register(ListenerEntry(PlayerInteractEvent::class,
            { true },
            {e ->
                actionTimestamp[e.player.uniqueId] = Bukkit.getCurrentTick()
            })
        )
        EventRegistry.register(ListenerEntry(EntityDamageByEntityEvent::class,
            { e -> e.damager is Player },
            {e ->
                actionTimestamp[e.damager.uniqueId] = Bukkit.getCurrentTick()
            })
        )
        EventRegistry.register(ListenerEntry(EntityDamageEvent::class,
            { e -> e.entity is Player },
            {e ->
                actionTimestamp[e.entity.uniqueId] = Bukkit.getCurrentTick()
            })
        )
        EventRegistry.register(ListenerEntry(PlayerJumpEvent::class,
            { true },
            {e ->
                actionTimestamp[e.player.uniqueId] = Bukkit.getCurrentTick()
            })
        )


    }

    override fun run() {
        val onlinePlayers = Bukkit.getOnlinePlayers()

        onlinePlayers
            .filter {
                !it.isAfk() &&
                Bukkit.getCurrentTick() - (actionTimestamp[it.uniqueId] ?: 0) < 1200
            }
            .forEach {
                incrementSurvived(it)
            }

        onlinePlayers.forEach {

            val survivedSeconds = it.getTag<Int>("survivaltime") ?: 0

            if (survivedSeconds >= MIN_BUFF_SECONDS) {
                val fraction = getBuffFraction(survivedSeconds)
                EffectManager.applyEffect(it, CustomEffectType.SURVIVAL_BUFFS,
                    EffectData(20 * 60, AttributeData(fraction * 10, Attribute.MINING_EFFICIENCY, AttributeModifier.Operation.ADD_NUMBER), true)
                )
            }

        }

    }
    fun getBuffFraction(seconds: Int): Double {
        return (seconds.coerceAtMost(MAX_BUFF_SECONDS) - MIN_BUFF_SECONDS).toDouble() / (MAX_BUFF_SECONDS - MIN_BUFF_SECONDS)
    }


    private fun incrementSurvived(player: Player) {
        val current = player.getTag<Int>("survivaltime") ?: 0
        player.setTag("survivaltime", current + 1)

        if (current == MAX_BUFF_SECONDS) {
            player.playerListName(Component.text(player.name, NamedTextColor.GOLD))
        }
        else if (current == MIN_BUFF_SECONDS) {
            player.playerListName(Component.text(player.name, NamedTextColor.YELLOW))
        }
    }
    private fun resetSurvived(player: Player) {
        player.setTag("survivaltime", 0)

        player.playerListName(Component.text(player.name, NamedTextColor.WHITE))
    }
    fun getSurvived(player: Player): Int {
        val current = player.getTag<Int>("survivaltime") ?: 0
        return current
    }

}