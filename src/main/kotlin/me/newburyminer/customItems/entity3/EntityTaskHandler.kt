package me.newburyminer.customItems.entity3

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils.Companion.getNearestPlayer
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.setTag
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.Mob
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

object EntityTaskHandler : BukkitRunnable() {

    fun registerTask(period: Int, task: Pair<CustomEntity, EntityTask>) {
        val periodTasks = tasks[period] ?: mutableListOf()
        periodTasks.add(task)
        tasks[period] = periodTasks
    }

    private val tasks = mutableMapOf<Int, MutableList<Pair<CustomEntity, EntityTask>>>()

    private const val AGGRO_RADIUS = 60.0
    override fun run() {
        tasks.forEach { (period, entityTasks) ->
            val currentTick = Bukkit.getCurrentTick()
            if (currentTick % period == 0) {
                for (player in Bukkit.getServer().onlinePlayers) {
                    if (player.world != CustomItems.aridWorld) continue
                    for (entity in player.getNearbyEntities(AGGRO_RADIUS, AGGRO_RADIUS, AGGRO_RADIUS)) {

                        if (entity.getTag<Int>("tick") == Bukkit.getServer().currentTick) continue
                        entity.setTag("tick", Bukkit.getServer().currentTick)

                        if (entity is Mob) updateTarget(entity)

                        runTaskOn(entity, entityTasks)

                    }
                }
            }
        }
    }

    private fun runTaskOn(entity: Entity, possibleEntities: List<Pair<CustomEntity, EntityTask>>) {
        val customEntity = CustomEntity.valueOf(entity.getTag<String>("id") ?: return)
        possibleEntities.forEach {
            if (it.first == customEntity)
                it.second.runTask(entity)
        }
    }

    private fun updateTarget(mob: Mob) {
        if (mob.target !is Player) {
            mob.target = mob.getNearestPlayer(AGGRO_RADIUS)
        }
    }

    fun cancelAll() {
        tasks.clear()
    }

    fun cancel(task: EntityTask) {
        tasks.forEach { (_, itemTasks) ->
            itemTasks.removeIf { task == it.second }
        }
    }

}