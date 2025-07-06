package de.joker.kutils.paper.mineskin

import com.google.gson.Gson
import de.joker.kutils.core.tools.Environment
import de.joker.kutils.paper.mineskin.models.MineSkinResponse
import de.joker.kutils.paper.mineskin.models.MineSkinSingleSkinResponse
import java.net.HttpURLConnection
import java.net.URI

object MineSkinFetcher {

    private val apiKey = Environment.getString("MINESKIN_API_KEY")

    private val gson = Gson()

    private val skinCache = mutableMapOf<String, MineSkinResponse>()

    fun fetchSkinSignature(skin: String): MineSkinResponse? {
        if (skinCache.containsKey(skin)) {
            return skinCache[skin]
        }

        val url = URI.create("https://api.mineskin.org/v2/skins/$skin").toURL()
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        conn.setRequestProperty("Accept", "application/json")
        conn.setRequestProperty("User-Agent", "MineSkin-User-Agent")
        conn.setRequestProperty("Authorization", "Bearer $apiKey")

        try {
            conn.connect()

            val responseCode = conn.responseCode
            if (responseCode != HttpURLConnection.HTTP_OK) {
                println("MineSkin API returned HTTP $responseCode")
                return null
            }

            val responseBody = conn.inputStream.bufferedReader().use { it.readText() }
            val sR = gson.fromJson(responseBody, MineSkinSingleSkinResponse::class.java)

            if (sR.skin == null) {
                return null
            }

            skinCache[skin] = sR.skin
            return sR.skin

        } finally {
            conn.disconnect()
        }
    }
}
