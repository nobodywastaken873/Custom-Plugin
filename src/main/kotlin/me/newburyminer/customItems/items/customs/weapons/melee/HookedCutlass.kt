package me.newburyminer.customItems.items.customs.weapons.melee

import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.*
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class HookedCutlass: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.HOOKED_CUTLASS

    private val material = Material.NETHERITE_SWORD
    private val color = arrayOf(61, 77, 87)
    private val name = text("Hooked Cutlass", color)
    private val lore = mutableListOf<Component>()

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    override fun handle(ctx: EventContext) {
        when (val e = ctx.event) {
            is EntityKnockbackByEntityEvent -> {
                if (ctx.itemType != EventItemType.MAINHAND) return
                val newKnockback = e.knockback.clone()
                newKnockback.x *= -0.8
                newKnockback.z *= -0.8
                e.knockback = newKnockback
            }
        }
    }

}