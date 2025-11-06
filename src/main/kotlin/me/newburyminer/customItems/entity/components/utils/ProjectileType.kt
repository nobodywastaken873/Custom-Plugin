package me.newburyminer.customItems.entity.components.utils

import org.bukkit.entity.Arrow
import org.bukkit.entity.Projectile
import org.bukkit.entity.SplashPotion

enum class ProjectileType(val clazz: Class<out Projectile>) {
    ARROW(Arrow::class.java),
    SPLASH_POTION(SplashPotion::class.java),

}