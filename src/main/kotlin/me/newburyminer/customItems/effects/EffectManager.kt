package me.newburyminer.customItems.effects

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.readName
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.attribute.AttributeModifier
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
        if (effectData.unique) {
            effectList.forEach {
                if (it.type == effectType && it.data == effectData) {
                    it.remaining = it.remaining.coerceAtLeast(effectData.duration)
                    return
                }
            }
        }
        effectList.add(effect)
        effect.behavior.onApply(player)
        effectMap[behavior.period] = effectList
        activeEffects[player.uniqueId] = effectMap
    }

    override fun run() {
        val currentTick = Bukkit.getCurrentTick()
        for ((uuid, map) in activeEffects) {
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

        if (currentTick % 20 == 0) {
            for (player in Bukkit.getOnlinePlayers()) {
                val activeEffects = getActiveEffects(player)
                var baseComponent = Utils.text("\nActive Effects:").style(
                    Style.style(TextColor.color(128, 196, 174),
                        TextDecoration.BOLD,
                    )
                )

                for (effect in activeEffects) {
                    baseComponent = baseComponent.append(toComponent(effect))
                }
                if (activeEffects.isEmpty())
                    baseComponent = baseComponent.append(Utils.text("\nNone", Utils.GRAY))

                player.sendPlayerListFooter(
                    baseComponent
                )
            }
        }
    }

    private fun toComponent(effect: ActiveEffect): Component {
        val text =
            if (effect.type == CustomEffectType.ATTRIBUTE) {
                val formattedAmount =
                    if (effect.data.attributeData!!.operation == AttributeModifier.Operation.ADD_NUMBER)
                        String.format("%+.6f", effect.data.attributeData.amount).trimEnd('0').trimEnd('.')
                    else
                        String.format("%+.6f", effect.data.attributeData.amount * 100).trimEnd('0').trimEnd('.') + "%"
                val formattedAttribute = effect.data.attributeData.attribute.readName()
                "\n$formattedAmount ${formattedAttribute} - ${effect.remaining/20}s"
            }
            else "\n${effect.type.title} - ${effect.remaining/20}s"

        val component = Utils.text(text, effect.type.color)
        return component
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