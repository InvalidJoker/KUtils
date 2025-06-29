/**
 * MIT License
 *
 * Copyright (c) 2024 CoasterFreakDE
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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