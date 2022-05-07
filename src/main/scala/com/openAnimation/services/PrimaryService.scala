package com.openAnimation.services

import org.springframework.stereotype.Service
import org.apache.commons.io.IOUtils
import java.io.{File, InputStream}

@Service
class PrimaryService {

  def getTapestry(): Array[Byte] = {
    val tapestryFilePath = new File(this.getClass.getClassLoader.getResource("tapestry").getPath).listFiles().last
    val videoStreamInput: InputStream = this.getClass.getClassLoader.getResourceAsStream(s"tapestry/${tapestryFilePath.getName}")
    val videoByteArr: Array[Byte] = IOUtils.toByteArray(videoStreamInput)
    videoByteArr
  }

}