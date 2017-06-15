package ru.u26c4.websocket

import java.io.Serializable

data class Message(val chat: String, val user: String, val text: String, val date: String) : Serializable

class Chat(guid: String) : Serializable {

    val guid: String = guid
        get

    var users = mutableListOf<String>()
        get

    var messages = mutableListOf<Message>()
        get

    override fun toString(): String {
        return "Chat: guid=$guid, users=${users.size}, messages=${messages.size}"
    }
}