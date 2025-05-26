package de.joker.paper.event.custom

import de.joker.paper.event.utils.KEvent
import org.bukkit.block.Block
import org.bukkit.entity.Player

class PlayerInteractAtBlockEvent(
    val player: Player,
    val block: Block,
) : KEvent()