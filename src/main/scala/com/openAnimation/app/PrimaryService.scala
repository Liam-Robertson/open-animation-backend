package com.openAnimation.app

import com.openAnimation.app.models.Snippet
import com.openAnimation.app.services.VideoStitchingService
import org.apache.commons.io.IOUtils
import org.springframework.stereotype.Service

import java.io.{File, InputStream};

@Service
class PrimaryService {

  val videoStitchingService = new VideoStitchingService()

  def addSnippetToTapestry(snippet: Snippet): String = {
    val duration = videoStitchingService.getDuration(snippet.startTime, snippet.endTime)
    val imageData = snippet.image.split("data:image/png;base64,")(1)
    val numFiles = new File(this.getClass.getClassLoader.getResource("images").getPath).listFiles().length + 1
    val imagePath = new File(this.getClass.getClassLoader.getResource("images").getPath + s"\\currentSnippet$numFiles.png").getPath
    videoStitchingService.saveImageToFilesystem(imageData, imagePath)
    videoStitchingService.createVideoFromImage(duration, imagePath)
    videoStitchingService.stitchSnippetIntoTapestry(snippet.startTime, snippet.endTime)
    "Image saved successfully"
  }

  def getTapestry: Array[Byte] = {
    val tapestryFilePath = new File(this.getClass.getClassLoader.getResource("tapestry").getPath).listFiles().last
    val videoStreamInput: InputStream = this.getClass.getClassLoader.getResourceAsStream(s"tapestry/${tapestryFilePath.getName}")
    val videoByteArr: Array[Byte] = IOUtils.toByteArray(videoStreamInput)
    videoByteArr
  }



}