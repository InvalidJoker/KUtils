package de.joker.kutils.paper.packets

import de.joker.kutils.paper.extensions.connection
import de.joker.kutils.paper.extensions.getPluginLogger
import dev.fruxz.ascend.extension.forceCast
import io.netty.channel.Channel
import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import net.minecraft.network.protocol.Packet
import org.bukkit.entity.Player
import org.jetbrains.annotations.ApiStatus

object PacketInterceptor {
    private val incomingCallbacks =
        mutableMapOf<Int, Pair<Class<out Packet<*>>, (Player, Packet<*>) -> Packet<*>?>>()
    private val outgoingCallbacks =
        mutableMapOf<Int, Pair<Class<out Packet<*>>, (Player, Packet<*>) -> Packet<*>?>>()
    private var counter = 0

    fun <T : Packet<*>> registerIncoming(
        packet: Class<T>,
        callback: (Player, T) -> Packet<*>?
    ): Int {
        val id = counter++
        incomingCallbacks[id] = Pair(packet) { player, pkt -> callback(player, pkt.forceCast()) }
        return id
    }

    fun <T : Packet<*>> registerOutgoing(
        packet: Class<T>,
        callback: (Player, T) -> Packet<*>?
    ): Int {
        val id = counter++
        outgoingCallbacks[id] = Pair(packet) { player, pkt -> callback(player, pkt.forceCast()) }
        return id
    }

    internal fun handleIncomingPacket(player: Player, packet: Packet<*>): Packet<*>? {
        var modified: Packet<*>? = null
        incomingCallbacks.forEach { (_, pair) ->
            val (clazz, cb) = pair
            if (clazz.isInstance(packet)) {
                modified = cb(player, packet)
            }
        }
        return modified
    }

    internal fun handleOutgoingPacket(player: Player, packet: Packet<*>): Packet<*>? {
        var modified: Packet<*>? = null
        outgoingCallbacks.forEach { (_, pair) ->
            val (clazz, cb) = pair
            if (clazz.isInstance(packet)) {
                modified = cb(player, packet)
            }
        }
        return modified
    }
}

/**
 * Injects a packet interceptor into the player.
 * You need to call this method on the player before you can receive packets.
 * This should be called in the PlayerJoinEvent.
 */
fun Player.injectPacketInterceptor() {
    val channelDuplexHandler = object : ChannelDuplexHandler() {

        override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
            var packet = msg
            try {
                if (packet is Packet<*>) {
                    PacketInterceptor.handleIncomingPacket(this@injectPacketInterceptor, packet)?.let {
                        packet = it
                    }
                }
            } catch (e: Exception) {
                getPluginLogger().warn("Error while reading packet")
                e.printStackTrace()
            }
            super.channelRead(ctx, packet)
        }

        override fun write(ctx: ChannelHandlerContext, msg: Any, promise: io.netty.channel.ChannelPromise) {
            var packet = msg
            try {
                if (packet is Packet<*>) {
                    PacketInterceptor.handleOutgoingPacket(this@injectPacketInterceptor, packet)?.let {
                        packet = it
                    }
                }
            } catch (e: Exception) {
                getPluginLogger().warn("Error while writing packet")
                e.printStackTrace()
            }
            super.write(ctx, packet, promise)
        }
    }

    val channel = connection.connection.channel
    channel.pipeline().addBefore("packet_handler", name, channelDuplexHandler)
}
