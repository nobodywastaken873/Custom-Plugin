package me.newburyminer.customItems.bosses

import me.newburyminer.customItems.CustomItems
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockExplodeEvent
import org.bukkit.event.block.BlockSpreadEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerTeleportEvent

class BossSystemHandler: Listener {

    @EventHandler fun onEntityDeath(e: EntityDeathEvent) {
        normalDeath(e)
    }
    // Prevent normal mobs from dropping items
    private fun normalDeath(e: EntityDeathEvent) {
        if (e.entity.location.world != CustomItems.bossWorld) return
        e.drops.clear()
    }

    // Prevent sculk spread in boss arena
    @EventHandler fun onSculkSpread(e: BlockSpreadEvent) {
        if (e.block.location.world == CustomItems.bossWorld) e.isCancelled = true
    }
    @EventHandler fun onArrowLand(e: ProjectileHitEvent) {
        if (e.entity.location.world != CustomItems.bossWorld) return
        if (e.entity !is AbstractArrow) return
        // Prevent arrows from accumulating on the ground
        if (e.hitBlock != null) {
            e.entity.remove()
        }
        // Prevent infighting
        else if (e.hitEntity != null) {
            if (e.hitEntity !is Player && e.entity.shooter !is Player) {
                e.isCancelled = true
            }
        }
    }
    // Prevent pearl glitching up through a ceiling
    @EventHandler fun onPlayerThrowPearl(e: ProjectileLaunchEvent) {
        if (e.entity.type != EntityType.ENDER_PEARL) return
        if (e.entity.world != CustomItems.bossWorld) return
        if (e.entity.shooter !is Player) return
        if ((e.entity.shooter as Player).location.block.isPassable) return
        e.isCancelled = true
    }
    // Prevent teleporting out of fight/dimension in any other way
    @EventHandler fun onPlayerTeleport(e: PlayerTeleportEvent) {
        if (e.player.world != CustomItems.bossWorld) return
        if (!e.to.block.isPassable && e.to.world == CustomItems.bossWorld) e.isCancelled = true
        if (e.to.world != CustomItems.bossWorld && e.cause == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) e.isCancelled = true
    }
    // Remove players from the boss world on logout, death
    @EventHandler fun onPlayerLogout(e: PlayerQuitEvent) {
        if (e.player.world != CustomItems.bossWorld) return
        BossManager.removePlayer(e.player)
    }
    @EventHandler fun onPlayerDeath(e: PlayerDeathEvent) {
        if (e.player.world != CustomItems.bossWorld) return
        BossManager.removePlayer(e.player)
    }
    // Cancel block breaks by players and explosions
    @EventHandler fun onBlockBreak(e: BlockBreakEvent) {
        if (e.player.world != CustomItems.bossWorld) return
        e.isCancelled = true
    }
    @EventHandler fun onEntityExplode(e: EntityExplodeEvent) {
        if (e.entity.world != CustomItems.bossWorld) return
        e.blockList().clear()
    }
    @EventHandler fun onBlockExplode(e: BlockExplodeEvent) {
        if (e.block.world != CustomItems.bossWorld) return
        e.blockList().clear()
    }

}