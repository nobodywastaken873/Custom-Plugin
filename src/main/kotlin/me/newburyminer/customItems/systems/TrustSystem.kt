package me.newburyminer.customItems.systems

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.getListTag
import me.newburyminer.customItems.Utils.Companion.setListTag
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.TextColor
import org.bukkit.entity.Player

object TrustSystem {

    fun addTrust(source: Player, toTrust: Player?) {
        if (toTrust == null) return

        val currentTrusted = source.getListTag<String>("trusted") ?: mutableListOf()
        val trustedSet = currentTrusted.toMutableSet()
        if (toTrust.name !in trustedSet) {
            trustedSet.add(toTrust.name)
            source.setListTag("trusted", trustedSet.toMutableList())
            source.sendMessage(Utils.text("You have trusted ${toTrust.name}.", Utils.SUCCESS_COLOR))
            toTrust.sendMessage(Utils.text("${source.name} has trusted you.", Utils.SUCCESS_COLOR))
        } else return

        val otherCurrentTrusted = toTrust.getListTag<String>("trusted") ?: mutableListOf()
        val otherTrustedSet = otherCurrentTrusted.toMutableSet()
        if (!otherTrustedSet.contains(source.name)) {
            toTrust.sendMessage(
                Utils.text("Click here to trust them back.", Utils.GRAY).clickEvent(
                    ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/trust add ${source.name}")
                )
            )
        }

    }

    fun removeTrust(source: Player, toUntrust: Player?) {
        if (toUntrust == null) return

        val currentTrusted = source.getListTag<String>("trusted") ?: mutableListOf()
        val trustedSet = currentTrusted.toMutableSet()
        if (!trustedSet.contains(toUntrust.name)) return
        trustedSet.remove(toUntrust.name)
        source.setListTag("trusted", trustedSet.toMutableList())

    }

    fun trusts(source: Player, isTrusted: Player): Boolean {

        val currentTrusted = source.getListTag<String>("trusted") ?: mutableListOf()
        val trustedSet = currentTrusted.toMutableSet()

        val otherCurrentTrusted = isTrusted.getListTag<String>("trusted") ?: mutableListOf()
        val otherTrustedSet = otherCurrentTrusted.toMutableSet()

        return trustedSet.contains(isTrusted.name) && otherTrustedSet.contains(source.name)

    }

}