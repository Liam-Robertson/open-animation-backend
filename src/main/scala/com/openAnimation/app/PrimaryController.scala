package com.openAnimation.app

import com.openAnimation.app.models.Snippet
import com.openAnimation.app.services.StartupService
import org.springframework.web.bind.annotation._

@CrossOrigin
@RestController
class PrimaryController(startupService: StartupService,
                        primaryService: PrimaryService) {

  @PostMapping(Array("/addSnippetToTapestry"))
  def addSnippetToTapestry(@RequestBody snippet: Snippet): String = primaryService.addSnippetToTapestry(snippet)

  @GetMapping(Array("/getTapestry"))
  def getTapestry: Array[Byte] = primaryService.getTapestry

  @GetMapping(Array("/"))
  def getServerStatusMessage: String = startupService.serverStatusMessage

}
