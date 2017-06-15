package ru.u26c4.websocket

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import redis.clients.jedis.Protocol
import redis.embedded.RedisServer

@Configuration
@EnableWebSocketMessageBroker
open class WebSocketChatConfig : AbstractWebSocketMessageBrokerConfigurer() {

    override fun configureMessageBroker(config: MessageBrokerRegistry?) {
        config?.enableSimpleBroker("/topic")
        config?.setApplicationDestinationPrefixes("/app")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry?) {
        registry?.addEndpoint("/chat")?.withSockJS()
    }
}

@Configuration
open class RedisConfig {

    @Bean
    open fun redisServer(): RedisServer {
        RedisServer.builder().reset()
        return RedisServer.builder().port(Protocol.DEFAULT_PORT).build()
    }

    @Bean
    open fun jedisConnectionFactory(): JedisConnectionFactory {
        return JedisConnectionFactory()
    }

    @Bean
    open fun redisTemplate(): RedisTemplate<String, Chat> {
        val template = RedisTemplate<String, Chat>()
        template.connectionFactory = jedisConnectionFactory()
        return template
    }
}