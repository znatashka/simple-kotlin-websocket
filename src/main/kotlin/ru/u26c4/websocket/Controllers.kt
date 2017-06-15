package ru.u26c4.websocket

import org.apache.commons.lang3.RandomStringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import java.text.SimpleDateFormat
import java.util.*

@Controller
class ChatController {

    @Autowired
    lateinit var redisTemplate: RedisTemplate<String, Chat>

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    fun greeting(text: String): Message {

        if (!redisTemplate.hasKey("chat")) redisTemplate.opsForValue().set("chat", Chat(RandomStringUtils.random(6, true, true).toUpperCase()))

        val chat = redisTemplate.opsForValue().get("chat")
        val message = Message(chat.guid, text, SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(Date()))
        chat.messages.add(message)

        redisTemplate.opsForValue().set("chat", chat)

        println(redisTemplate.opsForValue().get("chat"))

        return message
    }
}