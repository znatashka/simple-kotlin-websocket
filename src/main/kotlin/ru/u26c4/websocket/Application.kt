package ru.u26c4.websocket

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import redis.embedded.RedisServer
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@ComponentScan
@EnableAutoConfiguration
class Application {
    val log = LoggerFactory.getLogger(Application::class.java)!!

    @Autowired
    lateinit var redisServer: RedisServer

    @PostConstruct
    fun start() {
        log.info("starting redis...")
        if (!redisServer.isActive) redisServer.start()
        log.info("redis listen ports: ${redisServer.ports()}")
    }

    @PreDestroy
    fun stop() {
        log.info("shutting down redis...")
        redisServer.stop()
        log.info("bye!")
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}