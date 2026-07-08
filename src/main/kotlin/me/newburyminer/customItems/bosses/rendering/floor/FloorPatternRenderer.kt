package me.newburyminer.customItems.bosses.rendering.floor

import me.newburyminer.customItems.bosses.rendering.QuadRenderable
import me.newburyminer.customItems.bosses.rendering.Renderable
import me.newburyminer.customItems.bosses.rendering.shapes.Shape
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.util.Vector

class FloorPatternRenderer(
    private val mesher: ShapeMesher,
    val shape: Shape,
    private val world: World
): Renderable {

    override fun spawn(world: World) {
        update()
    }

    override fun update() {

        val mesh = mesher.mesh(shape)
        render(mesh)

    }

    private data class RenderTile(
        val quad: QuadRenderable,
        var tile: FloorTile?
    )

    private val active = mutableListOf<RenderTile>()

    private val inactive = ArrayDeque<RenderTile>()

    private fun obtain(): RenderTile {

        if (inactive.isNotEmpty())
            return inactive.removeFirst()

        val quad = QuadRenderable(
            origin = shape.center,
            center = Vector(),
            normal = Vector(0,1,0),
            up = Vector(0,0,-1),
            width = 1f,
            height = 1f,
            thickness = 0.05f,
            material = Material.AIR,
            smooth = false,
        )

        quad.spawn(world)

        return RenderTile(quad, null)
    }

    private fun release(tile: RenderTile) {

        tile.quad.deactivate()
        tile.tile = null

        inactive.addLast(tile)
    }

    //private val MAX_DISTANCE = 3.0
    private var lastVersion = -1
    private fun render(mesh: Mesh) {

        if (mesh.version == lastVersion)
            return

        lastVersion = mesh.version

        while (active.size > mesh.tiles.size) {

            release(active.removeLast())

        }

        while (active.size < mesh.tiles.size) {

            active += obtain()

        }

        for (i in mesh.tiles.indices) {

            val renderTile = active[i]
            val tile = mesh.tiles[i]

            renderTile.tile = tile

            val quad = renderTile.quad
            updateQuad(quad, tile)

        }

        /*val unmatched = active.toMutableList()
        val newActive = mutableListOf<RenderTile>()

        for (tile in mesh.tiles) {

            val best = unmatched
                //.filter {
                //    it.tile!!.center.distanceSquared(tile.center) < MAX_DISTANCE * MAX_DISTANCE
                //}
                .minByOrNull {
                    score(it.tile!!, tile)
                }

            val renderTile =
                if (best != null) {
                    unmatched.remove(best)
                    best
                } else {
                    obtain()
                }

            renderTile.tile = tile
            updateQuad(renderTile.quad, tile)

            newActive += renderTile
        }

        unmatched.forEach(::release)

        active.clear()
        active += newActive*/
    }

    private fun updateQuad(quad: QuadRenderable, tile: FloorTile) {
        quad.center = tile.center
        quad.normal = tile.normal
        quad.up = tile.up

        quad.width = tile.width.toFloat()
        quad.height = tile.height.toFloat()

        quad.material = tile.material

        quad.update()
        quad.activate()
    }

    override fun remove() {

        active.forEach {
            it.quad.remove()
        }

        inactive.forEach {
            it.quad.remove()
        }

        active.clear()
        inactive.clear()
    }

    /*private fun score(old: FloorTile, new: FloorTile): Double {

        //val distance = old.center.distanceSquared(new.center)

        val areaDiff = kotlin.math.abs(old.area - new.area)

        val aspectOld = old.width / old.height
        val aspectNew = new.width / new.height

        val aspectDiff = kotlin.math.abs(aspectOld - aspectNew)

        return areaDiff + aspectDiff
        //distance +
    }*/

}