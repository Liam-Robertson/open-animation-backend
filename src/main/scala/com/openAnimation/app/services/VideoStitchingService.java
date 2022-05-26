package com.openAnimation.app.services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import scala.Int;

import static java.lang.Math.floor;
import static java.lang.Math.round;

import static com.openAnimation.app.tools.CommandLine.executeCommand;
import static com.openAnimation.app.tools.CommandLine.executeFfmpeg;

@Service
public class VideoStitchingService {

    public String stitchSnippetIntoTapestry(String timeStart, String timeEnd) throws IOException, InterruptedException {
        String currentSnippetPath = new File(this.getClass().getClassLoader().getResource("working").getPath() + "/currentSnippet.mp4").getPath();
        List<String> videoList = trimVideo("tapestryPart1", "00:00", timeStart, new ArrayList<String>());
        videoList.add(String.format("file '%s'", currentSnippetPath));
        trimVideo("tapestryPart2", timeEnd, "05:00", videoList);
        this.stitchVideos(videoList);
        return "Successfully stitched new video into main animation!";
    }

    public void createVideoFromImage(String duration, String imagePath) throws IOException, InterruptedException {
        String outFile = new File(this.getClass().getClassLoader().getResource("working").getPath() + "\\currentSnippet.mp4").getPath();
//        String cmd = String.format("ffmpeg -y -framerate 1/5 -r 25 -loop 1 -i %s -c:v libx264 -t %s -pix_fmt yuv420p -vf scale=1080:720 %s", imagePath, duration, outFile);
        String[] cmd = {"ffmpeg", "-y", "-framerate", "1/5", "-r", "25", "-loop", "1", "-i", "imagePath", "-c:v", "libx264", "-t", "duration", "-pix_fmt", "yuv420p", "-vf", "scale=1080:720", "outFile"};
        System.out.println(cmd);
        executeFfmpeg(cmd);
    }

    public void saveImageToFilesystem(String imageUrl, String imagePath) throws IOException {
        byte[] decodedBytes = Base64.getDecoder().decode(imageUrl);
        FileUtils.writeByteArrayToFile(new File(imagePath), decodedBytes);
    }

    public String getDuration(String timeStartStr, String timeEndStr) {
        Double timeStart = convertTimeToSeconds(timeStartStr);
        Double timeEnd = convertTimeToSeconds(timeEndStr);
        Double durationSecs = timeEnd - timeStart;
        String duration = convertTimeToHHmmss(durationSecs);
        return duration;
    }

    public void stitchVideos(List<String> videoList) throws IOException, InterruptedException {
        Integer numOfTapestries =  new File(this.getClass().getClassLoader().getResource("tapestry").getPath()).listFiles().length;
        String textFile = new File(this.getClass().getClassLoader().getResource("working").getPath() + "/fileList.txt").getPath();
        String audioPath = new File(this.getClass().getClassLoader().getResource("static").getPath() + "/audiotrack.wav").getPath();
        String videoOutputPath = new File(this.getClass().getClassLoader().getResource("tapestry").getPath() + String.format("/tapestryVideo%s.mp4", numOfTapestries)).getPath();
        String finalOutputPath = new File(this.getClass().getClassLoader().getResource("tapestry").getPath() + String.format("/tapestry%s.mp4", numOfTapestries)).getPath();
        BufferedWriter bw = new BufferedWriter(new FileWriter(textFile));
        String videoTextList = String.join("\n", videoList);
        bw.write(videoTextList);
        bw.close();
        String[] cmd1 = {"ffmpeg", "-f", "concat", "-safe", "0", "-i", textFile, "-c", "copy", videoOutputPath};
        String[] cmd2 = {"ffmpeg", "-i", videoOutputPath, "-i", audioPath, "-shortest", "-c:v", "copy", "-c:a", "aac", finalOutputPath};
        System.out.println(String.join(" ", cmd1));
        System.out.println(String.join(" ", cmd2));
        executeFfmpeg(cmd1);
        executeFfmpeg(cmd2);
        new File(videoOutputPath).delete();
    }

    public List<String> trimVideo(String videoName, String startTime, String endTime, List<String> videoList) throws IOException, InterruptedException {
        String tapestryMp4 = new File(this.getClass().getClassLoader().getResource("tapestry/tapestry.mp4").getPath()).getPath();
        String outputPath = new File(new File(this.getClass().getClassLoader().getResource("working").getPath()).getPath() + String.format("/%s.mp4", videoName)).getPath();
        String[] cmd = {"ffmpeg", "-y", "-ss", startTime, "-to", endTime, "-i", tapestryMp4, "-c", "copy", "-an", outputPath};
        // -an means copy just video stream, not audio. -y means overwrite existing files
        System.out.println(String.join(" ", cmd));
        executeFfmpeg(cmd);
        String outPath = String.format("%s/%s.mp4", this.getClass().getClassLoader().getResource("working").getPath(), videoName);
        videoList.add(String.format("file '%s'", new File(outPath).getPath()));
        return videoList;
    }

    public String convertTimeToHHmmss(Double time) {
        Integer minutes = Math.toIntExact(round(floor(time / 60)));
        Double seconds = minutes > 0 ? time - (floor(time / 60) * 60) : time;
        return String.format("%s:%s", minutes, seconds);
    }

    public Double convertTimeToSeconds(String time) {
        Double minutes = Double.parseDouble(time.split(":")[0]);
        Double seconds = Double.parseDouble(time.split(":")[1]);
        Double timeSecs = (minutes * 60) + seconds;
        return timeSecs;
    }

}
