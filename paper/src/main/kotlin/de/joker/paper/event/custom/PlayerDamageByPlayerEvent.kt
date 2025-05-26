package de.joker.paper.event.custom

import de.joker.paper.event.utils.KEvent
import org.bukkit.entity.Player

class PlayerDamageByPlayerEvent(
    val damager: Player,
    val victim: Player,
    var damage: Double
) : KEvent()