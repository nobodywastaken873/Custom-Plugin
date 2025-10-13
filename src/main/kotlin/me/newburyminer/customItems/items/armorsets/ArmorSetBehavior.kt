package me.newburyminer.customItems.items.armorsets

interface ArmorSetBehavior {
    val set: ArmorSet
    fun handle(ctx: ArmorSetEventContext)
}