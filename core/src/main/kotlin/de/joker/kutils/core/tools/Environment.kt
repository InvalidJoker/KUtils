package de.joker.kutils.core.tools

import io.github.cdimascio.dotenv.dotenv

object Environment {

    private val env = System.getenv()
    private val dotEnv = dotenv {
        ignoreIfMissing = true
    }

    /**
     * Retrieves the value of the environment variable with the specified key.
     *
     * @param key The key of the environment variable.
     * @return The value of the environment variable, or null if the variable is not found.
     */
    fun getString(key: String): String? {
        return dotEnv[key] ?: env[key]
    }

    fun getBoolean(key: String): Boolean {
        return getString(key)?.toBoolean() == true
    }

    fun getInt(key: String): Int {
        return getString(key)?.toIntOrNull() ?: 0
    }

    fun getIntOrNull(key: String): Int? {
        return getString(key)?.toIntOrNull()
    }

    fun getIntOrDefault(key: String, default: Int): Int {
        return getIntOrNull(key) ?: default
    }

    fun getLong(key: String): Long {
        return getString(key)?.toLongOrNull() ?: 0L
    }

    fun getLongOrNull(key: String): Long? {
        return getString(key)?.toLongOrNull()
    }

    fun getLongOrDefault(key: String, default: Long): Long {
        return getLongOrNull(key) ?: default
    }

    fun getDouble(key: String): Double {
        return getString(key)?.toDoubleOrNull() ?: 0.0
    }

    fun getDoubleOrNull(key: String): Double? {
        return getString(key)?.toDoubleOrNull()
    }

    fun getDoubleOrDefault(key: String, default: Double): Double {
        return getDoubleOrNull(key) ?: default
    }

    fun getFloat(key: String): Float {
        return getString(key)?.toFloatOrNull() ?: 0.0f
    }

    fun getFloatOrNull(key: String): Float? {
        return getString(key)?.toFloatOrNull()
    }

    fun getFloatOrDefault(key: String, default: Float): Float {
        return getFloatOrNull(key) ?: default
    }

}