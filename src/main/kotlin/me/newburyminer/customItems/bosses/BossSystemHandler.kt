package me.newburyminer.customItems.bosses

import me.newburyminer.customItems.CustomItems
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockSpreadEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.player.PlayerQuitEvent

class BossSystemHandler: Listener {

    @EventHandler fun onEntityDeath(e: EntityDeathEvent) {
        normalDeath(e)
    }
    private fun normalDeath(e: EntityDeathEvent) {
        if (e.entity.location.world != CustomItems.bossWorld) return
        e.drops.clear()
    }

    @EventHandler fun onSculkSpread(e: BlockSpreadEvent) {
        if (e.block.location.world == CustomItems.bossWorld) e.isCancelled = true
    }
    @EventHandler fun onArrowLand(e: ProjectileHitEvent) {
        if (e.entity.location.world != CustomItems.bossWorld) return
        if (e.entity.type != EntityType.ARROW) return
        if (e.hitBlock != null) {
            e.entity.remove()
        } else if (e.hitEntity != null) {
            if (e.hitEntity !is Player && e.entity.shooter !is Player) {
                e.isCancelled = true
            }
        }
    }
    @EventHandler fun onPlayerThrowPearl(e: ProjectileLaunchEvent) {
        if (e.entity.type != EntityType.ENDER_PEARL) return
        if (e.entity.world != CustomItems.bossWorld) return
        if (e.entity.shooter !is Player) return
        if ((e.entity.shooter as Player).location.block.isPassable) return
        e.isCancelled = true
    }
    @EventHandler fun onPlayerLogout(e: PlayerQuitEvent) {
        if (e.player.world != CustomItems.bossWorld) return
        BossManager.removePlayer(e.player)
    }
    @EventHandler fun onPlayerDeath(e: PlayerDeathEvent) {
        if (e.player.world != CustomItems.bossWorld) return
        BossManager.removePlayer(e.player)
    }

}