package me.newburyminer.customItems.effects.factories

import me.newburyminer.customItems.effects.EffectBehavior
import me.newburyminer.customItems.effects.EffectData
import me.newburyminer.customItems.effects.EffectFactory
import me.newburyminer.customItems.effects.behaviors.DoubleGraveLootEffect
import me.newburyminer.customItems.effects.behaviors.ElytraDisableEffect
import me.newburyminer.customItems.effects.behaviors.PocketWormholeRemainingEffect

class PocketWormholeRemainingEffectFactory: EffectFactory {
    override fun create(effectData: EffectData): EffectBehavior {
        return PocketWormholeRemainingEffect()
    }
}