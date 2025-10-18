package me.newburyminer.customItems.gui

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.addItemorDrop
import me.newburyminer.customItems.Utils.Companion.getListTag
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.graveTeleportCooldown
import me.newburyminer.customItems.Utils.Companion.graveTeleportOnCooldown
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.recipes.Recipes
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.Tag
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

class GuiListeners: Listener {
    @EventHandler() fun onGuiInteract(e: InventoryClickEvent) {
        if (e.whoClicked !is Player) return
        val player = e.whoClicked
        if (e.inventory.holder !is GuiInventory) return
        if (e.action == InventoryAction.COLLECT_TO_CURSOR) {
            e.isCancelled = true
            return
        }
        if (e.currentItem == null) return
        //need to add locked tag to any item in recipes/etc., add an item to all slots in recipes to not delete items
        if (e.currentItem!!.getTag<Boolean>("locked") == true) {
            e.isCancelled = true
            return
        }
        val invName = (e.inventory.holder as GuiInventory).name
        if (invName != "craft") {
            if (e.clickedInventory != e.inventory) return
        }
        if (e.slot == 25 && e.currentItem!!.type != Material.LIGHT_GRAY_STAINED_GLASS_PANE && invName == "craft" && e.clickedInventory == e.inventory) {
            if (e.action != InventoryAction.PICKUP_ALL) {
                e.isCancelled = true
                return
            }
            var result = Recipes.checkForRecipe(e.inventory)
            //if result is not InventoryAction.PICKUP_ALL when clicking, cancel craft + event
            //if in gui and you try to collect to cursor (double click with item in cursor), cancel in any custom gui
            //prevent clicking in slot 25 from clearing crafting grid
            val slots = arrayOf(1, 2, 3, 4, 5, 10,11,12,13,14,19,20,21,22,23,28,29,30,31,32,37,38,39,40,41)
            var index = 0
            for (row in result!!.items) {
                for (item in row) {
                    if (e.inventory.getItem(slots[index]) == null) {++index; continue}
                    e.inventory.getItem(slots[index])!!.amount -= item?.getItem()?.amount ?: 0
                    ++index
                }
            }
            Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
                result = Recipes.checkForRecipe(e.inventory)
                if (result == null) e.inventory.setItem(25, GuiInventory.gray())
                else e.inventory.setItem(25, result!!.getResultItem())
                (e.whoClicked as Player).playSound(e.whoClicked, Sound.UI_TOAST_CHALLENGE_COMPLETE, 2.0F, 0.95F)
                //for (allplayer in Bukkit.getServer().onlinePlayers) allplayer.sendMessage(Utils.text("${(e.whoClicked as Player).name} has crafted ${result!!.resultItem.getCustom()!!.realName}"))
            })
        }
        if (invName.substring(0, invName.length-1) == "recipes" && e.currentItem!!.getTag<Int>("page") != null) {
            val page = e.currentItem!!.getTag<Int>("page")
            e.isCancelled = true
            Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
                val newInv = GuiInventory("recipes"+page.toString())
                newInv.inventory.contents = GuiInventory.recipes(page!!)
                player.closeInventory()
                player.openInventory(newInv.inventory)
            })
        }
        else if (invName.substring(0, invName.length-1) == "recipes") {
            val recipeSlots = arrayOf(1,2,3,4,5,6,7,10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,37,38,39,40,41,42,43)
            val page = invName[invName.length-1].digitToInt()
            val recipe = Recipes.getRecipe((page-1)*35 + recipeSlots.indexOf(e.slot))
            e.isCancelled = true
            Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
                val newInv = GuiInventory("recipe")
                newInv.inventory.contents = GuiInventory.recipe(recipe, page)
                player.closeInventory()
                player.openInventory(newInv.inventory)
            })
        }
        else if (invName == "recipe" && e.currentItem!!.getTag<Int>("page") != null) {
            val page = e.currentItem!!.getTag<Int>("page")
            e.isCancelled = true
            Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
                val newInv = GuiInventory("recipes"+page.toString())
                newInv.inventory.contents = GuiInventory.recipes(page!!)
                player.closeInventory()
                player.openInventory(newInv.inventory)
            })
        }
        else if (invName == "compass") {
            // iron, gold, diamonds, netherite, totems
            if (e.currentItem?.type != Material.COMPASS) return
            e.isCancelled = true
            val costs = e.currentItem!!.getListTag<Int>("costs")!!
            val neededAmounts = linkedMapOf(
                Pair(Material.RAW_IRON_BLOCK, costs[0]),
                Pair(Material.RAW_GOLD_BLOCK, costs[1]),
                Pair(Material.DIAMOND, costs[2]),
                Pair(Material.ANCIENT_DEBRIS, costs[3]),
                Pair(Material.TOTEM_OF_UNDYING, costs[4]),
            )
            val amounts = linkedMapOf(
                Pair(Material.RAW_IRON_BLOCK, 0),
                Pair(Material.RAW_GOLD_BLOCK, 0),
                Pair(Material.DIAMOND, 0),
                Pair(Material.ANCIENT_DEBRIS, 0),
                Pair(Material.TOTEM_OF_UNDYING, 0),
            )
            for (item in player.inventory) {
                if (item == null) continue
                if (item.type in amounts.keys) {
                    amounts[item.type] = amounts[item.type]!! + item.amount
                }
            }
            for (mat in amounts.keys) {
                if (neededAmounts[mat]!! > amounts[mat]!!) {
                    e.whoClicked.sendMessage(Utils.text("You do not have enough materials.", Utils.FAILED_COLOR))
                    (e.whoClicked as Player).playSound(e.whoClicked, Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F)
                    e.isCancelled = true
                    return
                }
            }
            for (item in player.inventory) {
                if (item == null) continue
                if (item.type !in neededAmounts.keys) continue
                val currentItemAmount = item.amount
                val currentNeededAmount = neededAmounts[item.type]!!
                if (currentNeededAmount == 0) continue
                if (currentNeededAmount >= currentItemAmount) {
                    neededAmounts[item.type] = neededAmounts[item.type]!! - item.amount
                    item.amount = 0
                } else {
                    item.amount -= neededAmounts[item.type]!!
                    neededAmounts[item.type] = 0
                }
            }
            e.whoClicked.setTag("lookingforname", true)
            e.whoClicked.sendMessage(Utils.text("Enter who you want to track: ", Utils.SUCCESS_COLOR))
            (e.whoClicked as Player).playSound(e.whoClicked, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F)
            Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
                player.closeInventory()
            })
        } else if (invName == "info") {
            e.isCancelled = true
            return
        }
    }

    /*@EventHandler fun onGraveInteract(e: InventoryClickEvent) {
        if (e.whoClicked !is Player) return
        if (e.inventory.holder !is GraveHolder) return
        (e.inventory.holder as GraveHolder).updateGrave()
        Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
            if (e.inventory.isEmpty) {
                (e.inventory.holder as GraveHolder).closeGrave()
                e.inventory.close()
                (e.inventory.holder as GraveHolder).deleteGrave()
            }
        })
    }
    // moved
    @EventHandler fun onGraveDrag(e: InventoryDragEvent) {
        if (e.whoClicked !is Player) return
        if (e.inventory.holder !is GraveHolder) return
        (e.inventory.holder as GraveHolder).updateGrave()
        if (e.inventory.isEmpty) {
            (e.inventory.holder as GraveHolder).closeGrave()
            Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
                e.inventory.close()
                (e.inventory.holder as GraveHolder).deleteGrave()
            })
        }
    }
    // moved
    @EventHandler fun onGraveClose(e: InventoryCloseEvent) {
        if (e.inventory.holder !is GraveHolder) return
        if (e.inventory.isEmpty) (e.inventory.holder as GraveHolder).deleteGrave()
        (e.inventory.holder as GraveHolder).closeGrave()
    }*/
    /*@EventHandler fun onGraveListInteract(e: InventoryClickEvent) {
        if (e.inventory.holder !is GraveListHolder) return
        e.isCancelled = true
        if (e.clickedInventory?.holder !is GraveListHolder) return
        val clickedItem = e.clickedInventory?.getItem(e.slot)
        if (clickedItem?.type == Material.ARROW) {
            Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
                e.whoClicked.closeInventory()
                e.whoClicked.openInventory(GraveListHolder(
                    e.whoClicked as Player,
                    e.clickedInventory?.getItem(e.slot)?.getTag<Int>("page") ?: 0
                ).inventory)
            })
        } else if (clickedItem?.type == Material.PAPER) {
            Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
                e.whoClicked.closeInventory()
                e.whoClicked.openInventory(GraveOptionsHolder(
                    e.whoClicked as Player,
                    (e.inventory.holder as GraveListHolder).getLocation(clickedItem),
                    (e.inventory.holder as GraveListHolder).page
                ).inventory)
            })
        }
    }
    @EventHandler fun onGraveOptionsInteract(e: InventoryClickEvent) {
        if (e.inventory.holder !is GraveOptionsHolder) return
        e.isCancelled = true
        if (e.clickedInventory?.holder !is GraveOptionsHolder) return
        val clickedItem = e.clickedInventory?.getItem(e.slot)
        val holder = e.inventory.holder as GraveOptionsHolder
        if (clickedItem?.type == Material.ARROW) {
            Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
                e.whoClicked.closeInventory()
                e.whoClicked.openInventory(GraveListHolder(
                    e.whoClicked as Player,
                    clickedItem.getTag<Int>("page") ?: 0
                ).inventory)
            })
        } else if (clickedItem?.type == Material.RED_CONCRETE) {
            Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
                holder.delete()
                e.whoClicked.closeInventory()
            })
        } else if (clickedItem?.type == Material.ENDER_PEARL) {
            if ((e.whoClicked as Player).graveTeleportOnCooldown()) {
                val cd = (e.whoClicked as Player).graveTeleportCooldown()
                //e.whoClicked.sendMessage(cd.toString())
                e.whoClicked.sendMessage(Utils.text("You cannot teleport to graves for another ${cd / 20 / 60}m, ${cd / 20 % 60}s.", Utils.FAILED_COLOR))
                e.whoClicked.sendMessage(Utils.text("After dying, you cannot teleport to any graves for 5 minutes.", Utils.FAILED_COLOR))
                (e.whoClicked as Player).playSound(e.whoClicked, Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F)
                e.isCancelled = true
                return
            }
            var cost = (e.whoClicked.getTag<Int>("totalgravetps") ?: 0) + 1
            var totalDias = 0
            for (item in e.whoClicked.inventory) {
                if (item == null) continue
                if (item.type != Material.DIAMOND) continue
                totalDias += item.amount
            }
            if (totalDias < cost) {
                e.whoClicked.sendMessage(Utils.text("You do not have enough materials.", Utils.FAILED_COLOR))
                (e.whoClicked as Player).playSound(e.whoClicked, Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F)
                e.isCancelled = true
                return
            }
            e.whoClicked.setTag("totalgravetps", (e.whoClicked.getTag<Int>("totalgravetps") ?: 0) + 1)
            for (item in e.whoClicked.inventory) {
                if (item == null) continue
                if (item.type != Material.DIAMOND) continue
                if (cost == 0) break
                if (cost >= item.amount) {
                    cost -= item.amount
                    item.amount = 0
                } else {
                    item.amount -= cost
                    cost = 0
                }
            }
            Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
                (e.whoClicked as Player).playSound(e.whoClicked, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F)
                holder.teleport()
                e.whoClicked.closeInventory()
            })
        }
    }*/

    @EventHandler fun onShulkerInteract(e: InventoryClickEvent) {
        if (e.whoClicked !is Player) return
        if (e.whoClicked.getTag<Boolean>("inventoryshulker") != true) return
        if (e.clickedInventory == null) return
        if (e.clickedInventory!!.getItem(e.slot)?.getTag<Boolean>("shulkeropen") == true) {
            e.isCancelled = true
            CustomEffects.playSound(e.whoClicked.location, Sound.ENTITY_SHULKER_HURT, 1.0F, 1.2F)
            return
        }
        if (e.inventory.holder !is ShulkerHolder) return
        if (!Tag.SHULKER_BOXES.isTagged((e.inventory.holder as ShulkerHolder).shulker.type)) {
            e.isCancelled = true
            Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
                e.whoClicked.closeInventory()
                return@Runnable
            })
        }
        (e.inventory.holder as ShulkerHolder).updateShulker()
    }
    @EventHandler fun onShulkerDrag(e: InventoryDragEvent) {
        if (e.whoClicked !is Player) return
        if (e.inventory.holder !is ShulkerHolder) return
        if (!Tag.SHULKER_BOXES.isTagged((e.inventory.holder as ShulkerHolder).shulker.type)) {
            e.isCancelled = true
        }
        (e.inventory.holder as ShulkerHolder).updateShulker()
    }
    @EventHandler fun onShulkerClose(e: InventoryCloseEvent) {
        if (e.inventory.holder !is ShulkerHolder) return
        (e.inventory.holder as ShulkerHolder).closeShulker()
    }

    @EventHandler fun onMaterialInteract(e: InventoryClickEvent) {
        val clickedInventory = e.clickedInventory ?: return
        val materialsHolder = e.inventory.holder as? MaterialsHolder ?: return
        val player = e.whoClicked as? Player ?: return
        if (clickedInventory.holder is MaterialsHolder) {
            e.isCancelled = true
            if (e.action in arrayOf(InventoryAction.PLACE_ALL, InventoryAction.PLACE_ONE, InventoryAction.PLACE_SOME, InventoryAction.SWAP_WITH_CURSOR)) {
                val toAdd = e.cursor
                if (materialsHolder.attemptInsert(toAdd)) e.cursor.amount = 0
            } else if (e.action in arrayOf(InventoryAction.PICKUP_ALL, InventoryAction.PICKUP_ONE, InventoryAction.PICKUP_SOME, InventoryAction.PICKUP_HALF)
                && clickedInventory.getItem(e.slot)?.type != Material.LIGHT_GRAY_STAINED_GLASS_PANE) {
                val icon = clickedInventory.getItem(e.slot) ?: return
                val amountToTake = materialsHolder.attemptRemove(icon)
                if (amountToTake == 0) return
                Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
                    if (player.itemOnCursor.type == Material.AIR) player.setItemOnCursor(ItemStack(icon.type, amountToTake))
                    else player.addItemorDrop(ItemStack(icon.type, amountToTake))
                })
            } else if (e.action == InventoryAction.MOVE_TO_OTHER_INVENTORY && clickedInventory.getItem(e.slot)?.type != Material.LIGHT_GRAY_STAINED_GLASS_PANE) {
                val icon = clickedInventory.getItem(e.slot) ?: return
                val amountToTake = materialsHolder.attemptRemove(icon)
                if (amountToTake == 0) return
                Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
                    player.addItemorDrop(ItemStack(icon.type, amountToTake))
                })
            }
        } else if (clickedInventory is PlayerInventory && e.action == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            val toAdd = clickedInventory.getItem(e.slot) ?: return
            if (materialsHolder.attemptInsert(toAdd)) toAdd.amount = 0
        }
    }

    //use persistent data container with bool to check whether to use nbt
    //schedule setting item after 1 tick
    //schedule clearing grid after 1 tick as well
    @EventHandler fun onGuiChange(e: InventoryClickEvent) {
        if (e.whoClicked !is Player) return
        val player = e.whoClicked
        if (e.inventory.holder !is GuiInventory) return
        if ((e.inventory.holder as GuiInventory).name == "craft") {
            Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
                val result = Recipes.checkForRecipe(e.inventory)
                if (result == null) e.inventory.setItem(25, GuiInventory.gray())
                else e.inventory.setItem(25, result.getResultItem())
            })
        }
    }
    @EventHandler fun onGuiChange(e: InventoryDragEvent) {
        if (e.whoClicked !is Player) return
        val player = e.whoClicked
        if (e.inventory.holder !is GuiInventory) return
        if ((e.inventory.holder as GuiInventory).name == "craft") {
            Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
                val result = Recipes.checkForRecipe(e.inventory)
                if (result == null) e.inventory.setItem(25, GuiInventory.gray())
                else e.inventory.setItem(25, result.getResultItem())
            })
        }
    }

    @EventHandler fun onGuiClose(e: InventoryCloseEvent) {
        if (e.inventory.holder == null) return
        if (e.inventory.holder!! !is GuiInventory) return
        val holder: GuiInventory = e.inventory.holder as GuiInventory
        if (holder.name != "craft") return
        for (slotNum in arrayOf(1, 2, 3, 4, 5, 10,11,12,13,14,19,20,21,22,23,28,29,30,31,32,37,38,39,40,41)) {
            val slot = e.inventory.getItem(slotNum) ?: continue
            if (slot.getTag<Boolean>("locked") == true) continue
            if (e.player.inventory.firstEmpty() == -1) {
                val item = e.player.world.spawn(e.player.location, Item::class.java)
                item.itemStack = slot
            } else {
                e.player.inventory.addItem(slot)
            }
        }
    }
}