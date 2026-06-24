package me.newburyminer.customItems.entity.components.utils

import org.bukkit.entity.Arrow
import org.bukkit.entity.BreezeWindCharge
import org.bukkit.entity.Projectile
import org.bukkit.entity.ShulkerBullet
import org.bukkit.entity.SmallFireball
import org.bukkit.entity.SplashPotion

enum class ProjectileType(val clazz: Class<out Projectile>) {
    ARROW(Arrow::class.java),
    SPLASH_POTION(SplashPotion::class.java),
    FIRE_CHARGE(SmallFireball::class.java),
    WIND_CHARGE(BreezeWindCharge::class.java),
    SHULKER_BULLET(ShulkerBullet::class.java),
}