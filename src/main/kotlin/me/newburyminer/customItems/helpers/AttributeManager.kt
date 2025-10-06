package me.newburyminer.customItems.helpers

import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils.Companion.decrementTag
import me.newburyminer.customItems.Utils.Companion.getListTag
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.setListTag
import me.newburyminer.customItems.Utils.Companion.setTag
import net.kyori.adventure.key.Key
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import kotlin.math.floor

class AttributeManager: Runnable {

    companion object {
        fun Player.tempAttribute(
            attribute: Attribute,
            modifier: AttributeModifier,
            duration: Double,
            tag: String,
            checkForDupe: Boolean = false
        ) {
            var key = NamespacedKey(CustomItems.plugin, "$attribute.${modifier.amount}.${modifier.operation}.$duration.$tag".lowercase())
            var list = if (duration == floor(duration)) {
                "second"
            } else {
                "tick"
            }
            if (checkForDupe) if ((this.getListTag<String>("attributes$list")
                    ?: mutableListOf()).contains(key.value())
            ) return
            else {
                key =  NamespacedKey(CustomItems.plugin, "$attribute.${modifier.amount}.${modifier.operation}.$duration.$tag".lowercase() + Math.random().toString())
            }
            val newModifier = AttributeModifier(key, modifier.amount, modifier.operation, modifier.slotGroup)
            this.getAttribute(attribute)!!.addModifier(newModifier)
            val newList = (this.getListTag<String>("attributes$list") ?: listOf()).toMutableList()
            newList.add(key.value())
            this.setListTag("attributes$list", newList)
            if (list == "second") {
                this.setTag(key.value(), duration.toInt())
            } else {
                this.setTag(key.value(), (duration * 20).toInt())
            }
        }
    }

    private var counter: Int = 0
    private lateinit var mainFuture: BukkitTask
    override fun run() {
        mainFuture = Bukkit.getScheduler().runTaskTimer(CustomItems.plugin, Runnable {
            counter = if (counter == 2400) 0 else counter + 1
            if ((counter % 20) == 0) {
                for (player in Bukkit.getServer().onlinePlayers) {
                    val attributes = (player.getListTag<String>("attributessecond") ?: listOf()).toMutableList()
                    val toRemove = mutableListOf<String>()
                    if (attributes.isNotEmpty()) {
                        for (attribute in attributes) {
                            player.decrementTag(attribute)
                            if (player.getTag<Int>(attribute) == 0) {
                                val attrString = attribute.subSequence(0, attribute.indexOf(".")).toString().lowercase()
                                val attr = RegistryAccess.registryAccess().getRegistry(RegistryKey.ATTRIBUTE).get(Key.key("minecraft:$attrString"))!!
                                player.getAttribute(attr)!!.removeModifier(NamespacedKey(CustomItems.plugin, attribute))
                                toRemove.add(attribute)
                            }
                        }
                    }
                    if (toRemove.isNotEmpty()) {
                        for (attr in toRemove) {
                            attributes.remove(attr)
                        }
                        player.setListTag("attributessecond", attributes)
                    }
                }
            }
            for (player in Bukkit.getServer().onlinePlayers) {
                val attributes = (player.getListTag<String>("attributestick") ?: listOf()).toMutableList()
                val toRemove = mutableListOf<String>()
                if (attributes.isNotEmpty()) {
                    for (attribute in attributes) {
                        player.decrementTag(attribute)
                        if (player.getTag<Int>(attribute) == 0) {
                            val attrString = attribute.subSequence(0, attribute.indexOf(".")).toString().lowercase()
                            val attr = RegistryAccess.registryAccess().getRegistry(RegistryKey.ATTRIBUTE).get(Key.key("minecraft:$attrString"))!!
                            player.getAttribute(attr)!!.removeModifier(NamespacedKey(CustomItems.plugin, attribute))
                            toRemove.add(attribute)
                        }
                    }
                }
                if (toRemove.isNotEmpty()) {
                    for (attr in toRemove) {
                        attributes.remove(attr)
                    }
                    player.setListTag("attributestick", attributes)
                }
            }
        }, 0L, 1L)
    }

    fun cancel() {
        mainFuture.cancel()
    }
}