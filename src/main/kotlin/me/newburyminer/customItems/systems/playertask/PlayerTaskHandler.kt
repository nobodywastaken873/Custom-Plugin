package me.newburyminer.customItems.systems.playertask

import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable

object PlayerTaskHandler : BukkitRunnable() {



    fun registerTask(period: Int, itemTask: PlayerTask) {
        val periodTasks = tasks[period] ?: mutableListOf()
        periodTasks.add(itemTask)
        tasks[period] = periodTasks
    }

    private val tasks = mutableMapOf<Int, MutableList<PlayerTask>>()

    override fun run() {
        tasks.forEach { (period, itemTasks) ->
            val currentTick = Bukkit.getCurrentTick()
            if (currentTick % period == 0) {
                for (player in Bukkit.getServer().onlinePlayers) {
                    itemTasks.forEach { it.run(player) }
                }
            }
        }
    }

    fun cancelAll() {
        tasks.clear()
    }

    fun cancel(task: PlayerTask) {
        tasks.forEach { (_, itemTasks) ->
            itemTasks.removeIf { task == it }
        }
    }

}