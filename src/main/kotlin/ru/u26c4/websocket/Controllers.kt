package ru.u26c4.websocket

import org.apache.commons.lang3.RandomStringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.stereotype.Controller
import java.text.SimpleDateFormat
import java.util.*

@Controller
class ChatController {

    @Autowired
    lateinit var redisTemplate: RedisTemplate<String, Any>

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    fun chat(@Payload text: String, headerAccessor: SimpMessageHeaderAccessor): Message {
        val sessionId = headerAccessor.sessionAttributes["sessionId"].toString()

        if (!redisTemplate.hasKey("chat")) {
            redisTemplate.opsForValue().set("chat", Chat(RandomStringUtils.random(6, true, true).toUpperCase()))
        }

        if (!redisTemplate.hasKey(sessionId)) {
            redisTemplate.opsForValue().set(sessionId, RandomStringUtils.random(6, true, false).toLowerCase().capitalize())
        }

        val chat = redisTemplate.opsForValue().get("chat") as Chat
        val user = redisTemplate.opsForValue().get(sessionId) as String

        val message = Message(
                chat.guid,
                user,
                text,
                SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(Date())
        )

        if (!chat.users.contains(user)) {
            chat.users.add(user)
        }
        chat.messages.add(message)

        redisTemplate.opsForValue().set("chat", chat)

        println("sessionId=$sessionId, ${redisTemplate.opsForValue().get("chat")}")

        return message
    }
}