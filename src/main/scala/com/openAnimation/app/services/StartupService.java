package com.openAnimation.app.services;

import com.openAnimation.app.tools.CommandLine;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.springframework.stereotype.Service;
import java.io.File;

import static com.openAnimation.app.tools.CommandLine.executeCommand;

@Service
public class StartupService {

    public void createInitialTapestry() throws IOException, InterruptedException {
        File tapestryFile = new File(this.getClass().getClassLoader().getResource("tapestry/tapestry.mp4").getPath());
        if (tapestryFile.exists()) {
            System.out.println(String.format("Tapestry already exists: %s", tapestryFile.getPath()));
        } else {
            System.out.println(createPlaceholderVideo(300));
        }
    }

    public String createPlaceholderVideo(Integer duration) throws IOException, InterruptedException {
        String imagePath = new File(this.getClass().getClassLoader().getResource("static/add-animation-sign.png").getPath()).getPath();
        String outFile = new File(this.getClass().getClassLoader().getResource("tapestry").getPath()).getPath() + "/tapestry.mp4";
        String audioPath = new File(this.getClass().getClassLoader().getResource("static/audiotrack.wav").getPath()).getPath();
        // frame rate 1/5 means each image lasts 5 seconds but -r 25 overrides this so that the frame rate is 25 fps
        // -shortest clips the audio to be the same length as the video
        // -c:v selects the codecs for the video to be libx264 which is a common encoder i.e. -(codecs):(video)
        String cmd = String.format("ffmpeg -framerate 1/5 -r 25 -loop 1 -i %s -i %s -c:v libx264 -t %s -pix_fmt yuv420p -vf scale=1080:720 -shortest %s", imagePath, audioPath, duration, outFile);
        System.out.println(cmd);
        String cmdOut = executeCommand(cmd);
        return cmdOut;
    }

    public String serverStatusMessage() {
        return "<div style='display:flex;padding-top:3em;justify-content:center;font-size:2em;'>" +
            "<h2 style=font-family:'Arial'>Server is up</h2>" +
        "</div>";
    }
}
