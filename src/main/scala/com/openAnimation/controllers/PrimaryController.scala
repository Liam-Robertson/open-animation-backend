package com.openAnimation.controllers

import com.openAnimation.models.Snippet
import com.openAnimation.services.{PrimaryService, StartupService}
import org.springframework.web.bind.annotation.{CrossOrigin, GetMapping, PostMapping, RequestBody, RestController}

@CrossOrigin
@RestController
class PrimaryController(startupService: StartupService,
                        primaryService: PrimaryService) {

  @PostMapping(Array("/addSnippetToTapestry"))
  def addSnippetToTapestry(@RequestBody snippet: Snippet): String = primaryService.addSnippetToTapestry(snippet)

  @GetMapping(Array("/getTapestry"))
  def getTapestry: Array[Byte] = primaryService.getTapestry()

  @GetMapping(Array("/"))
  def getServerStatusMessage: String = startupService.serverStatusMessage

}
