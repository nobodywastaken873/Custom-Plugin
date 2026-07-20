package me.newburyminer.customItems.loot

import me.newburyminer.customItems.Utils.Companion.GRAY
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.eventbus.EventRegistry
import me.newburyminer.customItems.eventbus.ListenerEntry
import me.newburyminer.customItems.helpers.FileDatabase
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack
import java.util.Base64
import java.util.UUID

object ItemClaimManager: FileDatabase() {

    fun registerEvents() {
        EventRegistry.register(
            ListenerEntry(PlayerJoinEvent::class, { e ->
                getAllItems(e.player).isNotEmpty()
            },
            { e ->
                e.player.sendMessage(text("You have items in your /itemclaims menu.", GRAY))
            })
        )
    }

    override val fileName: String = "itemClaims.txt"

    private val itemMap: MutableMap<UUID, MutableList<ItemStack>> = mutableMapOf()
    fun add(uuid: UUID, item: ItemStack) {
        itemMap.getOrPut(uuid) { mutableListOf() }.add(item)
    }
    fun remove(uuid: UUID, item: ItemStack) {
        itemMap.getOrPut(uuid) { mutableListOf() }.remove(item)
    }

    fun getAllItems(player: Player): List<ItemStack> {
        return itemMap[player.uniqueId] ?: emptyList()
    }

    override fun pushToFile(backup: Boolean) {
        val text = itemMap.map { (uuid, items) ->
            "${uuid}:" + items.joinToString(";") { item ->
                Base64.getEncoder().encodeToString(item.serializeAsBytes())
            }
        }.joinToString("\n")

        writeToFile(text, backup)
    }

    override fun initialize() {
        val text = readFromFile()
        if (text.isEmpty()) return

        val entries = text.split("\n")
        entries.forEach {entry ->
            val splitIndex = entry.indexOf(":")
            val uuid = UUID.fromString(entry.substring(0, splitIndex))
            val loot = entry.substring(splitIndex + 1).split(";").map {
                ItemStack.deserializeBytes(Base64.getDecoder().decode(it))
            }.toMutableList()
            itemMap[uuid] = loot
        }
    }

}