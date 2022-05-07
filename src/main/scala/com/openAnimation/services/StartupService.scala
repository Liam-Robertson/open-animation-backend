package com.openAnimation.services

import org.springframework.stereotype.Service

import java.io.File
import scala.sys.process.Process

@Service
class StartupService {

  def createInitialTapestry(): Unit = {
    val tryTapestryFile = Option(this.getClass.getClassLoader.getResource("tapestry/tapestry.mp4"))
    tryTapestryFile match {
      case Some(value) => println(s"Tapestry already exists: $value")
      case None => println(createPlaceholderVideo(300))
    }
  }

  def createPlaceholderVideo(duration: Int): String = {
    val imagePath = new File(this.getClass.getClassLoader.getResource("static/add-animation-sign.png").getPath).getPath
    val outFile = new File(this.getClass.getClassLoader.getResource("tapestry").getPath).getPath + "/tapestry.mp4"
    val audioPath = new File(this.getClass.getClassLoader.getResource("static/audiotrack.mp4").getPath).getPath
    // frame rate 1/5 means each image lasts 5 seconds but -r 25 overrides this so that the frame rate is 25 fps
    // -shortest clips the audio to be the same length as the video
    // -c:v selects the codecs for the video to be libx264 which is a common encoder i.e. -(codecs):(video)
    val cmd: String = s"ffmpeg -framerate 1/5 -r 25 -loop 1 -i ${imagePath} -i ${audioPath} -c:v libx264 -t ${duration} -pix_fmt yuv420p -shortest ${outFile}"
    val response: String = Process(cmd).!!
    response
  }

  val serverStatusMessage: String =
    "<div style='display:flex;padding-top:3em;justify-content:center;font-size:2em;'>" +
      "<h2 style=font-family:'Arial'>Server is up</h2>" +
    "</div>"

}
