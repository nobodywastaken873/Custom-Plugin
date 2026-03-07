package me.newburyminer.customItems.items.behaviors

import me.newburyminer.customItems.helpers.CustomEffects
import org.bukkit.Sound
import org.bukkit.entity.Player
import java.util.UUID

class HeldActivation(
    private val activationTicks: Int,
    //private val onTick: (Player, Int) -> Unit = {_, _ ->},
    //private val onEarlyReset: (Player) -> Unit = {CustomEffects.playSoundToPlayer(it, Sound.BLOCK_ANVIL_PLACE, 0.5F, 1.25F)},
    //private val onComplete: (Player, Int) -> Unit = {_, _ -> },
    private val resetOnActivate: Boolean = false,
    //private val onActivate: (Player) -> Unit = {}
) {
    private val dataMap = mutableMapOf<UUID , ActivationData>()

    private fun update(player: Player): ActivationResult {
        val uuid = player.uniqueId
        val state = dataMap[uuid] ?: return ActivationResult.Idle

        if (state.using) {
            state.ticks += 1
            state.using = false
            val currentTicks = state.ticks
            if (currentTicks >= activationTicks) {
                if (resetOnActivate) state.ticks = 0
                return ActivationResult.Activated(currentTicks)
            }
            else
                return ActivationResult.Charging(currentTicks)
        }

        if (state.ticks != 0) {
            val currentTicks = state.ticks
            state.ticks = 0
            return ActivationResult.Cancelled(currentTicks)
        }

        return ActivationResult.Idle
    }

    private var cachedResults: MutableMap<UUID, ActivationResult> = mutableMapOf()
    fun tick(player: Player): ActivationResult {
        val result = update(player)
        cachedResults[player.uniqueId] = result
        return result
    }

    fun currentResult(player: Player): ActivationResult {
        return cachedResults[player.uniqueId] ?: ActivationResult.Idle
    }

    fun used(player: Player) {
        val data = dataMap[player.uniqueId]
        if (data != null) data.using = true
        else dataMap[player.uniqueId] = ActivationData(using = true)
    }

    //fun isActive(player: Player): Boolean {
    //    return (dataMap[player.uniqueId]?.ticks ?: 0) >= activationTicks
    //}

    sealed class ActivationResult {
        object Idle : ActivationResult()
        data class Charging(val ticks: Int) : ActivationResult()
        data class Activated(val ticks: Int) : ActivationResult()
        data class Cancelled(val ticks: Int) : ActivationResult()
    }

    data class ActivationData(
        var ticks: Int = 0,
        var using: Boolean = false
    )

}