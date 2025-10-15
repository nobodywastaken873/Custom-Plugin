package me.newburyminer.customItems.effects

import me.newburyminer.customItems.effects.behaviors.ElytraDisableEffect
import me.newburyminer.customItems.effects.factories.*

object CustomEffectBootstrapper {
    fun registerAll() {
        EffectRegistry.registerBulk(
            mapOf(
                CustomEffectType.ATTRIBUTE to AttributeEffectFactory(),
                CustomEffectType.ELYTRA_DISABLED to ElytraDisableEffectFactory(),
                CustomEffectType.ENDER_CRIT to EnderCritEffectFactory(),
                CustomEffectType.FANG_STAFF_VEXING to FangStaffVexingEffectFactory(),
                CustomEffectType.GRAVE_INVULNERABILITY to GraveInvulnerabilityEffectFactory(),
                CustomEffectType.LAST_PRISM_ZAP to LastPrismZapEffectFactory(),
            )
        )

        EffectEventHandler.register(CustomEffectType.ELYTRA_DISABLED, ElytraDisableEffect())

    }
}