package de.joker.kutils.core.api

import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.net.URI
import java.util.*

object MojangAPI {
    private val gson = Gson()

    fun getUser(user: String): Player? {
        try {
            CoroutineScope(Dispatchers.Default).run {
                val url = "https://playerdb.co/api/player/minecraft/$user"
                val response = URI.create(url).toURL().readText()

                val errorResponse = gson.fromJson(response, PlayerResponse::class.java)
                if (!errorResponse.success) {
                    return null
                }

                val res = gson.fromJson(response, PlayerResponse::class.java)

                return res.data.player
            }
        } catch (e: Exception) {
            return null
        }
    }
}

fun usernameToRealUUID(input: String): UUID? {
    MojangAPI.getUser(input)?.let {
        return UUID.fromString(it.id)
    }
    return null
}

data class PlayerResponse(
    val code: String,
    val message: String,
    val data: PlayerData,
    val success: Boolean
)

data class PlayerData(
    val player: Player
)

data class Player(
    val username: String,
    val id: String,
    val raw_id: String,
    val avatar: String,
    val skin_texture: String,
    val properties: List<PlayerProperty>,
    val name_history: List<String>
)

data class PlayerProperty(
    val name: String,
    val value: String,
    val signature: String
)