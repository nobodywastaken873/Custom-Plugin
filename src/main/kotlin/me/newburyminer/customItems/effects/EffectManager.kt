package me.newburyminer.customItems.effects

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

object EffectManager: BukkitRunnable() {

    private val activeEffects = mutableMapOf<UUID, MutableMap<Int, MutableList<ActiveEffect>>>()

    data class ActiveEffect(
        val behavior: EffectBehavior,
        var remaining: Int,
        val data: EffectData,
        val type: CustomEffectType
    )

    fun applyEffect(player: Player, effectType: CustomEffectType, duration: Int) {
        val effectData = EffectData(duration)
        applyEffect(player, effectType, effectData)
    }

    fun applyEffect(player: Player, effectType: CustomEffectType, effectData: EffectData) {
        val behavior = EffectRegistry.create(effectType, effectData) ?: return
        val effectMap = activeEffects[player.uniqueId] ?: mutableMapOf()
        val effectList = effectMap[behavior.period] ?: mutableListOf()
        val effect = ActiveEffect(behavior, effectData.duration, effectData, effectType)
        if (effectData.unique && effectList.any {it.type == effectType && it.data == effectData}) return
        effectList.add(effect)
        effect.behavior.onApply(player)
        effectMap[behavior.period] = effectList
        activeEffects[player.uniqueId] = effectMap
    }

    override fun run() {
        for ((uuid, map) in activeEffects) {
            val currentTick = Bukkit.getCurrentTick()
            val player = Bukkit.getPlayer(uuid) ?: continue
            map.forEach { (period, list) ->
                if (currentTick % period == 0) {
                    val iterator = list.iterator()
                    while (iterator.hasNext()) {
                        val effect = iterator.next()
                        effect.remaining -= period
                        effect.behavior.onTick(player)
                        if (effect.remaining <= 0) {
                            effect.behavior.onRemove(player)
                            iterator.remove()
                        }
                    }
                }
            }
        }
    }

    fun getActiveEffects(player: Player): List<ActiveEffect> {
        val effectList = mutableListOf<ActiveEffect>()
        val map = activeEffects[player.uniqueId] ?: return effectList
        map.forEach { (_, list) ->
            list.forEach {
                effectList.add(it)
            }
        }
        return effectList.toList()
    }

    fun hasEffect(player: Player, type: CustomEffectType): Boolean {
        return getActiveEffects(player).any { it.type == type }
    }

    private fun removeEffect(player: Player, activeEffect: ActiveEffect) {
        val effectMap = activeEffects[player.uniqueId] ?: return
        effectMap.forEach { (_, list) ->
            list.removeIf { it == activeEffect }
        }
    }

    fun removeEffect(player: Player, type: CustomEffectType? = null) {
        val activeEffects = getActiveEffects(player)
        activeEffects.forEach {
            if (it.type == type || type == null) {
                removeEffect(player, it)
            }
        }
    }


}