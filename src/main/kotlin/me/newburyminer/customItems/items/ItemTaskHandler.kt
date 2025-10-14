package me.newburyminer.customItems.items

import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable

object ItemTaskHandler : BukkitRunnable() {



    fun registerTask(period: Int, itemTask: ItemTask) {
        val periodTasks = tasks[period] ?: mutableListOf()
        periodTasks.add(itemTask)
        tasks[period] = periodTasks
    }

    private val tasks = mutableMapOf<Int, MutableList<ItemTask>>()

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

    fun cancel(task: ItemTask) {
        tasks.forEach { (_, itemTasks) ->
            itemTasks.removeIf { task == it }
        }
    }

}