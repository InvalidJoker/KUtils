package de.joker.paper.ux.visuals

import de.joker.paper.coroutines.taskRunTimer
import de.joker.paper.ux.visuals.base.VisualElement
import de.joker.paper.ux.visuals.base.VisualManager
import dev.fruxz.stacked.text
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.key.Key
import org.bukkit.entity.Player
import kotlin.time.Duration.Companion.seconds

object BossBarVisuals : VisualManager {
    private val store = VisualsStore(5)
    private val buff = StringBuilder()
    private var bossBarStore = mutableMapOf<Player, BossBar>()
    private val prefix: String = "\uF82A<color:#FFFFFF>[</color> "
    private val suffix: String = " <color:#FFFFFF>]</color>"
    private val fontKey = Key.key("serverbase", "default")

    private fun createBossBar(player: Player): BossBar {
        val bar = BossBar.bossBar(text(""), 0.0f, BossBar.Color.YELLOW, BossBar.Overlay.PROGRESS)
        bossBarStore[player] = bar
        bar.addViewer(player)
        return bar
    }

    override fun addVisual(player: Player, visual: VisualElement) {
        if (!bossBarStore.containsKey(player)) {
            createBossBar(player)
        }
        store.addVisual(player, visual)
    }


    override fun removeVisual(player: Player, id: String) {
        store.removeVisual(player, id)
        if (!store.hasVisual(player)) {
            val bar = bossBarStore.remove(player)
            bar?.removeViewer(player)
        }
    }


    override fun removePlayer(player: Player) =
        store.removePlayer(player)

    fun runBossBar() {
        taskRunTimer(20, "Render BossBar") {
            store.renderAll(1.seconds).forEach {
                buff.setLength(0)
                it.second.forEach { s -> buff.append(s ?: "").append(' ') }
                val player = it.first
                val bar = bossBarStore[player] ?: error("No BossBar for $player")
                bar.name(text(prefix + buff.toString().trim() + suffix).font(fontKey))
            }
        }
    }
}