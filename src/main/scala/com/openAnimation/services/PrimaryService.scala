package com.openAnimation.services

import com.openAnimation.models.Snippet
import org.apache.commons.io.{FileUtils, IOUtils}
import org.springframework.stereotype.Service

import java.io.{File, InputStream}
import java.util.Base64;

@Service
class PrimaryService {

  def addSnippetToTapestry(snippet: Snippet): String = {
    val imageData = snippet.image.split("data:image/png;base64,")(1)
    val imagePath = new File(this.getClass.getClassLoader.getResource("tapestry").getPath + "\\test.png")
    saveImage(imageData, imagePath)
    "Image saved successfully"
  }

  def getTapestry: Array[Byte] = {
    val tapestryFilePath = new File(this.getClass.getClassLoader.getResource("tapestry").getPath).listFiles().last
    val videoStreamInput: InputStream = this.getClass.getClassLoader.getResourceAsStream(s"tapestry/${tapestryFilePath.getName}")
    val videoByteArr: Array[Byte] = IOUtils.toByteArray(videoStreamInput)
    videoByteArr
  }

  def saveImage(imageUrl: String, imagePath: File): Unit = {
    val decodedBytes = Base64.getDecoder.decode(imageUrl);
    FileUtils.writeByteArrayToFile(imagePath, decodedBytes);
  }

}