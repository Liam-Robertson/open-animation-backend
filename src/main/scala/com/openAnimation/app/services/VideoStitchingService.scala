package com.openAnimation.app.services

import org.apache.commons.io.FileUtils

import java.io.{BufferedWriter, File, FileWriter}
import java.text.SimpleDateFormat
import java.util.{Base64, Calendar, Date}
import scala.math.floor
import scala.sys.process.ProcessLogger
import scala.util.Try
import scala.sys.process._

class VideoStitchingService {

  def stitchSnippetIntoTapestry(timeStart: String, timeEnd: String): String = {
    val currentSnippetPath = new File(this.getClass.getClassLoader.getResource("working").getPath + "/currentSnippet.mp4").getPath
    val videoList1 = this.trimVideo("tapestryPart1", "00:00", timeStart, Array())
    val videoList2 = videoList1 :+  s"file '${currentSnippetPath}'"
    val videoList3 = this.trimVideo("tapestryPart2", timeEnd, "05:00", videoList2)
    this.stitchVideos(videoList3)
    "Successfully stitched new video into main animation!"
  }

  def createVideoFromImage(duration: String, imagePath: String): Unit = {
    val outFile = new File(this.getClass.getClassLoader.getResource("working").getPath + "\\currentSnippet.mp4").getPath
    val cmd: String = s"ffmpeg -y -framerate 1/5 -r 25 -loop 1 -i ${imagePath} -c:v libx264 -t ${duration} -pix_fmt yuv420p -vf scale=1080:720 ${outFile}"
    println(cmd)
    val exitCode = cmd ! ProcessLogger(stdout append _, stderr append _ + "\n")
    if (exitCode != 0) {
      println(stderr.toString)
      throw new Exception("Failed to stitch new video into tapestry")
    }
  }

  def saveImageToFilesystem(imageUrl: String, imagePath: String): Unit = {
    val decodedBytes = Base64.getDecoder.decode(imageUrl);
    FileUtils.writeByteArrayToFile(new File(imagePath), decodedBytes);
  }

  def getDuration(timeStartStr: String, timeEndStr: String): String = {
    val timeStart = this.convertTimeToSeconds(timeStartStr)
    val timeEnd = this.convertTimeToSeconds(timeEndStr)
    val durationSecs = timeEnd - timeStart
    val duration = this.convertTimeToHHmmss(durationSecs)
    duration
  }

  def stitchVideos(videoList: Array[String]): Unit = {
    val stdout = new StringBuilder
    val stderr = new StringBuilder
    val numOfTapestries =  new File(this.getClass.getClassLoader.getResource("tapestry").getPath).listFiles().length
    val textFile = new File(this.getClass.getClassLoader.getResource("working").getPath + "/fileList.txt").getPath
    val audioPath = new File(this.getClass.getClassLoader.getResource("static").getPath + "/audiotrack.wav").getPath
    val videoOutputPath = new File(this.getClass.getClassLoader.getResource("tapestry").getPath + s"/tapestryVideo${numOfTapestries}.mp4").getPath
    val finalOutputPath = new File(this.getClass.getClassLoader.getResource("tapestry").getPath + s"/tapestry${numOfTapestries}.mp4").getPath
    val bw = new BufferedWriter(new FileWriter(textFile))
    val videoTextList: String = videoList.mkString("\n")
    bw.write(videoTextList)
    bw.close()
    val cmd1 = s"""ffmpeg -f concat -safe 0 -i $textFile -c copy $videoOutputPath"""
    println(cmd1)
    val exitCodeVideo = cmd1 ! ProcessLogger(stdout append _, stderr append _ + "\n")
    if (exitCodeVideo != 0) {
      println(stderr.toString)
      throw new Exception("Failed to stitch new video into tapestry")
    }
    val cmd2 = s"""ffmpeg -i $videoOutputPath -i $audioPath -shortest -c:v copy -c:a aac ${finalOutputPath}"""
    print(cmd2)
    val exitCodeAudio = cmd2 ! ProcessLogger(stdout append _, stderr append _ + "\n")
    if (exitCodeAudio != 0) {
      println(stderr.toString)
      throw new Exception("Failed to stitch audio to new video")
    }
    new File(videoOutputPath).delete
  }

  def trimVideo(videoName: String, startTime: String, endTime: String, videoList: Array[String]): Array[String] = {
    val stdout = new StringBuilder
    val stderr = new StringBuilder
    val tapestryMp4 = new File(this.getClass.getClassLoader.getResource("tapestry/tapestry.mp4").getPath).getPath
    val outputPath = new File(new File(this.getClass.getClassLoader.getResource("working").getPath).getPath + s"/$videoName.mp4").getPath
    println(s"""ffmpeg -y -ss $startTime -to $endTime  -i $tapestryMp4 -c copy -an $outputPath""")
    // -an means copy just video stream, not audio. -y means overwrite existing files
    val exitCode = s"""ffmpeg -y -ss $startTime -to $endTime  -i $tapestryMp4 -c copy -an $outputPath""" ! ProcessLogger(stdout append _, stderr append _ + "\n")
    if (exitCode != 0) {
      println(stderr.toString)
      throw new Exception(s"Failed trim video $videoName")
    } else {
      val outPath = s"${this.getClass.getClassLoader.getResource("working").getPath}/$videoName.mp4"
      val finalVideoList = videoList :+ s"file '${new File(outPath).getPath}'"
      finalVideoList
    }
  }

  def convertTimeToHHmmss(time: Double): String = {
    val timeStrTry = Try {
      val minutes: Int = floor(time / 60).toInt
      val seconds: Double = if (minutes > 0) time - (floor(time / 60) * 60) else time
      s"$minutes:$seconds"
    }
    timeStrTry.getOrElse(throw new Exception(s"Failed to convert $time to hours:minutes:seconds format"))
  }

  def convertTimeToSeconds(time: String): Double = {
    val minutes = time.split(":")(0).toDouble
    val seconds = time.split(":")(1).toDouble
    val timeSecs: Double = (minutes * 60) + seconds
    timeSecs
  }

}
