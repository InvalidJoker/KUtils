package de.joker.kutils.core.api

import dev.fruxz.ascend.json.globalJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import java.net.URI
import java.util.*

object MojangAPI {
    fun getUser(user: String): MojangPlayer? {
        try {
            CoroutineScope(Dispatchers.Default).run {
                val url = "https://playerdb.co/api/player/minecraft/$user"
                val response = URI.create(url).toURL().readText()


                val jsonResponse = globalJson.decodeFromString<PlayerResponse>(response)

                if (!jsonResponse.success) {
                    return null
                }

                return jsonResponse.data.player
            }
        } catch (e: Exception) {
            return null
        }
    }

    @Serializable
    data class PlayerResponse(
        val code: String,
        val message: String,
        val data: PlayerData,
        val success: Boolean
    )

    @Serializable
    data class PlayerData(
        val player: MojangPlayer
    )

    @Serializable
    data class MojangPlayer(
        val username: String,
        val id: String,
        val raw_id: String,
        val avatar: String,
        val skin_texture: String,
        val properties: List<PlayerProperty>,
        val name_history: List<String>
    )

    @Serializable
    data class PlayerProperty(
        val name: String,
        val value: String,
        val signature: String
    )
}

fun usernameToRealUUID(input: String): UUID? {
    MojangAPI.getUser(input)?.let {
        return UUID.fromString(it.id)
    }
    return null
}