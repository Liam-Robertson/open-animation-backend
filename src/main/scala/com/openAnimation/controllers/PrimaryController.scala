package com.openAnimation.controllers

import com.openAnimation.services.{PrimaryService, StartupService}
import org.springframework.web.bind.annotation.{CrossOrigin, GetMapping, RestController}

@CrossOrigin
@RestController
class PrimaryController(startupService: StartupService,
                        primaryService: PrimaryService) {

  @GetMapping(Array("/getTapestry"))
  def getTapestry: Array[Byte] = primaryService.getTapestry()

  @GetMapping(Array("/"))
  def getServerStatusMessage: String = startupService.serverStatusMessage

}
