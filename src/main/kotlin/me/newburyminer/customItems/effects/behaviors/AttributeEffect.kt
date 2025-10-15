package me.newburyminer.customItems.effects.behaviors

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.effects.AttributeData
import me.newburyminer.customItems.effects.EffectBehavior
import org.bukkit.NamespacedKey
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player
import java.util.*

open class AttributeEffect(private val attributeData: AttributeData): EffectBehavior {

    val key: NamespacedKey

    init {
        val shortId = UUID.randomUUID().toString().substring(0, 8)
        key = NamespacedKey(CustomItems.plugin, "AttributeEffect:${attributeData.attribute},${attributeData.amount},${shortId}")
    }

    override fun onApply(player: Player) {
        val modifier = AttributeModifier(key, attributeData.amount, attributeData.operation)
        player.getAttribute(attributeData.attribute)?.modifiers?.add(modifier)
    }

    override fun onRemove(player: Player) {
        player.getAttribute(attributeData.attribute)?.removeModifier(key)
    }
}