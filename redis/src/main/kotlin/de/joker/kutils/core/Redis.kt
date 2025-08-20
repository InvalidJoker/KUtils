package de.joker.kutils.core

import de.joker.kutils.core.notifications.EventBus
import de.joker.kutils.core.notifications.Notification
import dev.fruxz.ascend.extension.logging.getThisFactoryLogger
import dev.fruxz.ascend.json.globalJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig
import redis.clients.jedis.JedisPubSub

lateinit var redisCalls: RedisCalls

class RedisCalls(
    val host: String = System.getenv("REDIS_HOST") ?: "localhost",
    val port: Int = System.getenv("REDIS_PORT")?.toInt() ?: 6379,
    val prefix: String = System.getenv("REDIS_PREFIX") ?: "notifications"
) {
    init {
        redisCalls = this
    }
    private lateinit var jedisPool: JedisPool
    private lateinit var jedisPubSub: JedisPubSub

    val eventBus = EventBus()

    val logger = getThisFactoryLogger()

    val json = Json {
        classDiscriminator = "_type"
        ignoreUnknownKeys = true
    }

    val redisScope = CoroutineScope(Dispatchers.IO)

    @OptIn(InternalSerializationApi::class)
    fun connect() {
        logger.info("Connecting to Redis server...")

        val jedisPoolConfig = JedisPoolConfig()
        jedisPoolConfig.maxTotal = 50
        jedisPoolConfig.maxIdle = 5
        jedisPoolConfig.minIdle = 1
        jedisPoolConfig.testOnBorrow = true
        jedisPool = JedisPool(jedisPoolConfig, host, port)

        jedisPubSub = object : JedisPubSub() {
            override fun onPMessage(pattern: String?, channel: String?, message: String?) {
                if (channel == null || message.isNullOrEmpty()) return

                val event = runCatching {
                    json.decodeFromString(Notification.serializer(), message)
                }.getOrElse {
                    logger.error("Failed to decode message: $message", it)
                    return
                }

                eventBus.dispatch(event)
            }
        }

        redisScope.launch {
            jedisPool.resource.use { jedis ->
                jedis.psubscribe(jedisPubSub, "$prefix:*")
            }
        }

        logger.info("Redis client initialized successfully.")
        logger.info("Subscribed to all channels.")
    }

    fun <T> getHash(key: String, serializer: KSerializer<T>): Map<String, T> {
        return try {
            jedisPool.resource.use { jedis ->
                val map = jedis.hgetAll(key)
                map.mapValues { (_, value) ->
                    globalJson.decodeFromString(serializer, value)
                }
            }
        } catch (e: Exception) {
            logger.error("Failed to get hash '$key'", e)
            emptyMap()
        }
    }

    fun <T> addToHash(key: String, field: String, value: T, serializer: KSerializer<T>) {
        try {
            jedisPool.resource.use { jedis ->
                val serializedValue = globalJson.encodeToString(serializer, value)
                jedis.hset(key, field, serializedValue)
            }
        } catch (e: Exception) {
            logger.error("Failed to add field '$field' with value '$value' to hash '$key'", e)
        }
    }

    fun <T> getFromHash(key: String, field: String, serializer: KSerializer<T>): T? {
        return try {
            jedisPool.resource.use { jedis ->
                val value = jedis.hget(key, field) ?: return null
                globalJson.decodeFromString(serializer, value)
            }
        } catch (e: Exception) {
            logger.error("Failed to get field '$field' from hash '$key'", e)
            null
        }
    }

    fun removeFromHash(key: String, field: String) {
        try {
            jedisPool.resource.use { jedis ->
                jedis.hdel(key, field)
            }
        } catch (e: Exception) {
            logger.error("Failed to remove field '$field' from hash '$key'", e)
        }
    }

    @OptIn(InternalSerializationApi::class)
    fun notify(event: Notification) {
        try {
            jedisPool.resource.use { jedis ->
                val jsonStr = json.encodeToString(Notification.serializer(),event)
                jedis.publish("$prefix:${event.channel}", jsonStr)
            }
        } catch (e: Exception) {
            logger.error("Failed to publish event to channel '${event.channel}'", e)
        }
    }

    fun <T> get(key: String, serializer: KSerializer<T>): T? {
        return try {
            jedisPool.resource.use { jedis ->
                val value = jedis.get(key) ?: return null
                globalJson.decodeFromString(serializer, value)
            }
        } catch (e: Exception) {
            logger.error("Failed to get key '$key'", e)
            null
        }
    }

    fun <T> set(key: String, value: T, serializer: KSerializer<T>) {
        try {
            jedisPool.resource.use { jedis ->
                val serialized = globalJson.encodeToString(serializer, value)
                jedis.set(key, serialized)
            }
        } catch (e: Exception) {
            logger.error("Failed to set key '$key'", e)
        }
    }

    fun close() {
        logger.info("Closing Redis connection...")
        if (jedisPubSub.isSubscribed) {
            jedisPubSub.unsubscribe()
        }
        jedisPool.close()
        logger.info("Redis connection closed.")
    }
}