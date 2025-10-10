package me.newburyminer.customItems.items

enum class EventItemType {
    MAINHAND,
    OFFHAND,
    HAND,
    HELMET,
    CHESTPLATE,
    LEGGINGS,
    BOOTS,
    PROJECTILE,
    SUMMONED_ENTITY,
    UNKNOWN
    ;

    fun isHand(): Boolean {
        return this in arrayOf(MAINHAND, OFFHAND, HAND)
    }
}