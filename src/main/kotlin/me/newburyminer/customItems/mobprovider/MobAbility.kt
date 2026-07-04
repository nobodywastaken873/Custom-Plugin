package me.newburyminer.customItems.mobprovider

interface MobAbility {

    fun MobBuilder.applyAbility(
        ctx: MobContext
    )

}