package ru.u26c4.websocket

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.server.HandshakeInterceptor
import redis.clients.jedis.Protocol
import redis.embedded.RedisServer
import java.lang.Exception

class HttpHandshakeInterceptor : HandshakeInterceptor {

    override fun beforeHandshake(request: ServerHttpRequest?,
                                 response: ServerHttpResponse?,
                                 wsHandler: WebSocketHandler?,
                                 attributes: MutableMap<String, Any>?): Boolean {
        when (request) {
            is ServletServerHttpRequest -> {
                val session = request.servletRequest.session
                attributes?.put("sessionId", session.id)
            }
        }
        return true
    }

    override fun afterHandshake(p0: ServerHttpRequest?, p1: ServerHttpResponse?, p2: WebSocketHandler?, p3: Exception?) {
    }
}

@Configuration
@EnableWebSocketMessageBroker
open class WebSocketChatConfig : AbstractWebSocketMessageBrokerConfigurer() {

    override fun configureMessageBroker(config: MessageBrokerRegistry?) {
        config?.enableSimpleBroker("/topic")
        config?.setApplicationDestinationPrefixes("/app")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry?) {
        registry?.addEndpoint("/chat")?.
                addInterceptors(HttpHandshakeInterceptor())?.
                withSockJS()
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
    open fun redisTemplate(): RedisTemplate<String, Any> {
        val template = RedisTemplate<String, Any>()
        template.connectionFactory = jedisConnectionFactory()
        return template
    }
}