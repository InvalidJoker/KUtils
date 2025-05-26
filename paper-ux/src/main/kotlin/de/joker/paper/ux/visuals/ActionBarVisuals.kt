package de.joker.paper.ux.visuals

import de.joker.paper.coroutines.taskRunTimer
import de.joker.paper.extensions.actionBar
import de.joker.paper.ux.visuals.base.VisualElement
import de.joker.paper.ux.visuals.base.VisualManager
import org.bukkit.entity.Player
import kotlin.time.Duration.Companion.seconds

object ActionBarVisuals : VisualManager {
    private val store = VisualsStore(5)
    private val buff = StringBuilder()

    override fun addVisual(player: Player, visual: VisualElement) =
        store.addVisual(player, visual)

    override fun removeVisual(player: Player, id: String) =
        store.removeVisual(player, id)

    override fun removePlayer(player: Player) =
        store.removePlayer(player)

    fun runActionBars() {
        taskRunTimer(20, "Render ActionBar") {
            store.renderAll(1.seconds).forEach {
                buff.setLength(0)
                it.second.forEach { s -> buff.append(s ?: "").append(' ') }
                it.first.actionBar(buff.toString())
            }
        }
    }
}