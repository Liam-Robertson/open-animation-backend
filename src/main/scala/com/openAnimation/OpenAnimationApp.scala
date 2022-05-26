package com.openAnimation

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class OpenAnimationApp

object OpenAnimationApp extends App {
  val startupService = new StartupService1

  val response = try {
    startupService.createInitialTapestry()
  } catch {
    case exception: Exception => println(s"\n${exception}\n")
  }
  SpringApplication.run(classOf[OpenAnimationApp]);
}

