package me.newburyminer.customItems.effects.factories

import me.newburyminer.customItems.effects.EffectBehavior
import me.newburyminer.customItems.effects.EffectData
import me.newburyminer.customItems.effects.EffectFactory
import me.newburyminer.customItems.effects.behaviors.DoubleChestLootEffect
import me.newburyminer.customItems.effects.behaviors.DoubleGraveLootEffect
import me.newburyminer.customItems.effects.behaviors.ElytraDisableEffect
import me.newburyminer.customItems.effects.behaviors.MobEnragedEffect
import me.newburyminer.customItems.effects.behaviors.QuadrupleChestLootEffect

class MobEnragedEffectFactory: EffectFactory {
    override fun create(effectData: EffectData): EffectBehavior {
        return MobEnragedEffect()
    }
}