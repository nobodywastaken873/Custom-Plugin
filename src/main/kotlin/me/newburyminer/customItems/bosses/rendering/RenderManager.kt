package me.newburyminer.customItems.bosses.rendering

class RenderManager {

    private val renderables = mutableListOf<Renderable>()

    fun add(renderable: Renderable) {
        renderables += renderable
    }

    fun tick() {

        renderables.forEach(Renderable::update)

    }

    fun clear() {

        renderables.forEach(Renderable::remove)

        renderables.clear()
    }

}