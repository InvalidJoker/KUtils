package de.joker.kutils.paper.event.custom

import de.joker.kutils.paper.event.utils.KEvent
import org.bukkit.block.Block
import org.bukkit.entity.Player

class PlayerInteractAtBlockEvent(
    val player: Player,
    val block: Block,
) : KEvent()
