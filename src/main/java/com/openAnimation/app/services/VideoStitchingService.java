package com.openAnimation.app.services;

import com.openAnimation.app.models.Snippet;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import static java.lang.Math.floor;
import static java.lang.Math.round;

import static com.openAnimation.app.tools.CommandLine.*;

@Service
public class VideoStitchingService {

    public String stitchSnippetIntoTapestry(String timeStart, String timeEnd) throws IOException, InterruptedException {
        String tapestryDuration = getTapestryDuration().toString();
        List<String> videoList = new ArrayList<String>();
        String currentSnippetPath = new File(this.getClass().getClassLoader().getResource("working").getPath() + "/currentSnippet.mp4").getPath();
        if (!Objects.equals(timeStart, "0.0")) {
            trimVideo("tapestryPart1", "0.0", timeStart, videoList);
        }
        videoList.add(String.format("file '%s'", currentSnippetPath));
        trimVideo("tapestryPart2", timeEnd, tapestryDuration, videoList);
        this.stitchVideos(videoList, tapestryDuration);
        return "Successfully stitched new video into main animation!";
    }

    public void createVideoFromImage(String duration, String imagePath) throws IOException, InterruptedException {
        String outFile = new File(this.getClass().getClassLoader().getResource("working").getPath() + "/currentSnippet.mp4").getPath();
//        String cmd = String.format("ffmpeg -y -framerate 1/5 -r 25 -loop 1 -i %s -c:v libx264 -t %s -pix_fmt yuv420p -vf scale=1080:720 %s", imagePath, duration, outFile);
        String[] cmd = {"ffmpeg", "-y", "-framerate", "1/5", "-r", "25", "-loop", "1", "-i", imagePath, "-c:v", "libx264", "-t", duration, "-pix_fmt", "yuv420p", "-vf", "scale=1080:720", outFile};
        System.out.println(String.join(" ", cmd));
        executeFfmpeg(cmd);
    }

    public void saveImageToFilesystem(String imageUrl, String imagePath) throws IOException {
        byte[] decodedBytes = Base64.getDecoder().decode(imageUrl);
        FileUtils.writeByteArrayToFile(new File(imagePath), decodedBytes);
    }

    public void formatTimestamp(Snippet snippet) {
        snippet.setStartTime(convertTimeToSeconds(snippet.getStartTime()).toString());
        snippet.setEndTime(convertTimeToSeconds(snippet.getEndTime()).toString());
        addToTimestamp(snippet);
    }

    public String getDuration(String timeStartStr, String timeEndStr) {
        Double durationSecs = Double.parseDouble(timeEndStr) - Double.parseDouble(timeStartStr);
//        String duration = convertTimeToHHmmss(durationSecs);
        return durationSecs.toString();
    }

    public void stitchVideos(List<String> videoList, String tapestryDuration) throws IOException, InterruptedException {
        Integer numOfTapestries =  new File(this.getClass().getClassLoader().getResource("tapestry").getPath()).listFiles().length;
        Integer numOfNoAudioVids =  new File(this.getClass().getClassLoader().getResource("tapestry-no-audio").getPath()).listFiles().length;
        String textFile = new File(this.getClass().getClassLoader().getResource("working").getPath() + "/fileList.txt").getPath();
        String audioPath = new File(this.getClass().getClassLoader().getResource("static").getPath() + "/audiotrack.wav").getPath();
        String audioPathOut = new File(this.getClass().getClassLoader().getResource("working").getPath() + "/audiotrack.wav").getPath();
        String videoOutputPath = new File(this.getClass().getClassLoader().getResource("tapestry-no-audio").getPath() + String.format("/tapestryNoAudio%s.mp4", numOfNoAudioVids + 2)).getPath();
        String finalOutputPath = new File(this.getClass().getClassLoader().getResource("tapestry").getPath() + String.format("/tapestry%s.mp4", numOfTapestries + 1)).getPath();
        BufferedWriter bw = new BufferedWriter(new FileWriter(textFile));
        String videoTextList = String.join("\n", videoList);
        bw.write(videoTextList);
        bw.close();
//        String[] cmd2 = {"ffmpeg", "-i", videoOutputPath, "-i", audioPath, "-shortest", "-c:v", "copy", finalOutputPath};
//        ffmpeg -i video.mp4 -i audio.wav -map 0:v -map 1:a -c:v copy -shortest output.mp4
//        String[] cmd2 = {"ffmpeg", "-i", videoOutputPath, "-i", audioPath, "-map", "0:v", "-map", "1:a", "-c:v", "copy", "-shortest", finalOutputPath};
//        String[] cmd3 = {"ffmpeg", "-y", "-i", videoOutputPath, "-i", audioPathOut, finalOutputPath};
        String[] cmd1 = {"ffmpeg", "-y", "-f", "concat", "-safe", "0", "-i", textFile, "-c", "copy", videoOutputPath};
        String[] cmd2 = {"ffmpeg", "-y", "-i", audioPath, "-ss", "0", "-t", tapestryDuration, audioPathOut};
        String[] cmd3 = {"ffmpeg", "-y", "-i", videoOutputPath, "-i", audioPathOut, "-map", "0:v", "-map", "1:a", "-c:v", "copy", finalOutputPath};
        System.out.println(String.join(" ", cmd1));
        System.out.println(String.join(" ", cmd2));
        System.out.println(String.join(" ", cmd3));
        executeFfmpeg(cmd1);
        executeFfmpeg(cmd2);
        executeFfmpeg(cmd3);
    }

    public List<String> trimVideo(String videoName, String startTime, String endTime, List<String> videoList) throws IOException, InterruptedException {
        String numOfFiles = String.valueOf(new File(this.getClass().getClassLoader().getResource("tapestry").getPath()).listFiles().length);
        String tapestryMp4 = new File(this.getClass().getClassLoader().getResource(String.format("tapestry/tapestry%s.mp4", numOfFiles)).getPath()).getPath();
        String outputPath = new File(new File(this.getClass().getClassLoader().getResource("working").getPath()).getPath() + String.format("/%s.mp4", videoName)).getPath();
        String[] cmd = {"ffmpeg", "-y", "-ss", startTime, "-to", endTime, "-i", tapestryMp4, "-c", "copy", "-an", outputPath};
        // -an means copy just video stream, not audio. -y means overwrite existing files
        System.out.println(String.join(" ", cmd));
        executeFfmpeg(cmd);
        String outPath = String.format("%s/%s.mp4", this.getClass().getClassLoader().getResource("working").getPath(), videoName);
        videoList.add(String.format("file '%s'", new File(outPath).getPath()));
        return videoList;
    }

    public void addToTimestamp(Snippet snippet) {
        Double newEndTime = Double.parseDouble(snippet.getEndTime()) + 0.1;
        snippet.setEndTime(newEndTime.toString());
    }

    public Double getTapestryDuration() throws IOException, InterruptedException {
        Integer numOfTapestries =  new File(this.getClass().getClassLoader().getResource("tapestry").getPath()).listFiles().length;
        String vidPath = new File(this.getClass().getClassLoader().getResource("tapestry").getPath()).listFiles()[numOfTapestries - 1].getPath();
        String[] cmd = new String[]{"ffprobe", "-i", vidPath, "-v", "quiet", "-show_entries", "format=duration", "-hide_banner", "-of", "default=noprint_wrappers=1:nokey=1"};
        Double tapestryDuration = Double.parseDouble(getFfmpegOutput(cmd));
        return tapestryDuration;
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
