package de.joker.kutils.paper.mineskin

import java.net.HttpURLConnection
import java.net.URI

object MineSkinFetcher {

    private val apiKey: String? = System.getenv("MINESKIN_API_KEY")

    private val skinCache = mutableMapOf<String, MineSkinResponse>()

    fun fetchSkinSignature(skin: String): MineSkinResponse? {
        if (skinCache.containsKey(skin)) {
            return skinCache[skin]
        }

        val url = URI.create("https://api.mineskin.org/v2/skins/$skin").toURL()
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        conn.setRequestProperty("Accept", "application/json")
        conn.setRequestProperty("User-Agent", "KUtils-MineSkinFetcher/1.0")
        conn.setRequestProperty("Authorization", "Bearer $apiKey")

        conn.connect()

        val responseCode = conn.responseCode
        if (responseCode != 200) {
            conn.disconnect()
            return null
        }

        val responseBody = conn.inputStream.bufferedReader().use { it.readText() }
        conn.disconnect()

        // minimal JSON parsing, assuming the shape:
        // {
        //   "skin": {
        //     "id": ...,
        //     "uuid": "...",
        //     "name": "...",
        //     "data": {
        //       "texture": "...",
        //       "signature": "..."
        //     }
        //   }
        // }

        val skinObjStart = responseBody.indexOf("\"skin\"")
        if (skinObjStart == -1) {
            return null
        }

        val textureValue = extractJsonString(responseBody, "\"texture\"")
        val signatureValue = extractJsonString(responseBody, "\"signature\"")

        if (textureValue == null || signatureValue == null) {
            return null
        }

        val skinResponse = MineSkinResponse(
            texture = textureValue,
            signature = signatureValue
        )

        skinCache[skin] = skinResponse

        return skinResponse
    }

    private fun extractJsonString(json: String, key: String): String? {
        val keyIndex = json.indexOf(key)
        if (keyIndex == -1) return null
        val colonIndex = json.indexOf(':', keyIndex)
        if (colonIndex == -1) return null
        val firstQuote = json.indexOf('"', colonIndex + 1)
        if (firstQuote == -1) return null
        val secondQuote = json.indexOf('"', firstQuote + 1)
        if (secondQuote == -1) return null
        return json.substring(firstQuote + 1, secondQuote)
    }
}

data class MineSkinResponse(
    val texture: String,
    val signature: String
)
