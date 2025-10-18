package me.newburyminer.customItems.items.customs.tools.misc.jetpack

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.attr
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.isInCombat
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.removeAllAttributes
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

class Jetpack: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.JETPACK

    private val material = Material.NETHERITE_CHESTPLATE
    private val color = arrayOf(255, 158, 3)
    private val name = text("Jetpack", color)
    private val lore = Utils.loreBlockToList(
        text("Equip and use the jetpack controller to control your movement.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .noNoiseEquippable(EquipmentSlot.CHEST)
        .build()

    override fun handle(ctx: EventContext) {}

    override val extraTasks: Map<Int, (Player) -> Unit>
        get() = mapOf(1 to {player -> updateJetpack(player)})

    private fun updateJetpack(player: Player) {
        val jetpackEquipped = player.inventory.chestplate?.isItem(CustomItem.JETPACK) ?: false
        val jetpackActive = player.getTag<Boolean>("jetpackactive") ?: false
        if (jetpackActive && player.isInCombat()) {
            player.setTag("jetpackactive", false)
            player.playSound(player, Sound.ITEM_SHIELD_BREAK, 1.0F, 1.0F)
        }
        if (!jetpackEquipped && !jetpackActive) return
        if (!jetpackActive) {
            player.inventory.chestplate!!.removeAllAttributes()
            player.inventory.chestplate!!.attr("ARM+8.0CH","ART+3.0CH","KNR+0.1CH")
        } else if (jetpackEquipped) {
            var controller: ItemStack? = null
            if (player.inventory.itemInOffHand.isItem(CustomItem.JETPACK_CONTROLLER)) controller = player.inventory.itemInOffHand
            if (player.inventory.itemInMainHand.isItem(CustomItem.JETPACK_CONTROLLER)) controller = player.inventory.itemInMainHand
            val realUpVel = player.y - (player.getTag<Double>("prevyval") ?: player.y)
            player.setTag("prevyval", player.y)
            val upVel = player.velocity.y
            val controllerActive =
                if (controller == null) {false}
                else if (player.activeItemUsedTime > 0) {true}
                else {false}


            //player.sendMessage(controllerActive.toString())
            //player.sendMessage(upVel.toString())
            //player.sendMessage(realUpVel.toString())
            //player.sendMessage(player.isSneaking.toString())
            val grav: Double
            if /* acc up */ (controllerActive && realUpVel * 20 < 2.5) {
                grav = -0.08 + -0.08
                //Bukkit.getLogger().info("acc up")
            } /* constant up */ else if (controllerActive) {
                grav = -0.02 / 0.98 * realUpVel + -0.08
                //Bukkit.getLogger().info("constant up")
            } /* acc down */ else if (player.isSneaking && realUpVel * 20 > -2.5) {
                grav = 0.08 + -0.08
                //Bukkit.getLogger().info("acc down")
            } /* constant down */ else if (player.isSneaking) {
                grav = -0.02 / 0.98 * realUpVel + -0.08
                //Bukkit.getLogger().info("constant down")
            } /* slowing up */ else if (!player.isSneaking && realUpVel < -0.01) {
                grav = 0.1 * realUpVel + -0.08
                //Bukkit.getLogger().info("slowing up")
            } /* slowing down */ else if (realUpVel > 0.01) {
                grav = 0.1 * realUpVel + -0.08
                //Bukkit.getLogger().info("slowing down")
            } /* hovering */ else {
                grav = 0.0 + -0.08
                //Bukkit.getLogger().info("hovering")
            }
            val gravityString = if (grav >= 0.0) "+$grav" else grav.toString()
            player.inventory.chestplate!!.removeAllAttributes()
            player.inventory.chestplate!!.attr("ARM+8.0CH","ART+3.0CH","KNR+0.1CH", "GRA${gravityString}CH", "FAD-1.0CH")
        }
    }

}