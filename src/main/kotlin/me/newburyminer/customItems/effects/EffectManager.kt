package me.newburyminer.customItems.effects

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.UUID

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
    }

    override fun run() {
        for ((uuid, map) in activeEffects) {
            val currentTick = Bukkit.getCurrentTick()
            val player = Bukkit.getPlayer(uuid) ?: continue
            map.forEach { (period, list) ->
                if (currentTick % period == 0) {
                    val iterator = list.iterator()
                    iterator.forEachRemaining { effect ->
                        effect.remaining -= period
                        effect.behavior.onTick(player)
                        if (effect.remaining <= 0) effect.behavior.onRemove(player)
                        list.remove(effect)
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


}