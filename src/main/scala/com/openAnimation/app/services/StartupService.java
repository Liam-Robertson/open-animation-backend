package com.openAnimation.app.services;

import com.openAnimation.app.tools.CommandLine;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import java.io.File;

import static com.openAnimation.app.tools.CommandLine.*;

@Service
public class StartupService {

    @EventListener(ApplicationReadyEvent.class)
    public void createInitialTapestry() throws IOException, InterruptedException {
        URL tapestryFile = this.getClass().getClassLoader().getResource("tapestry/tapestry1.mp4");
        if (tapestryFile != null) {
            System.out.println(String.format("Tapestry already exists: %s", tapestryFile.getPath()));
        } else {
            createPlaceholderVideo(300);
        }
    }

    public void createPlaceholderVideo(Integer duration) throws IOException, InterruptedException {
        String imagePath = new File(this.getClass().getClassLoader().getResource("static/add-animation-sign.png").getPath()).getPath();
        String outFile = new File(new File(this.getClass().getClassLoader().getResource("tapestry").getPath()).getPath() + "/tapestry1.mp4").getPath();
        String audioPath = new File(this.getClass().getClassLoader().getResource("static/audiotrack.wav").getPath()).getPath();
        // frame rate 1/5 means each image lasts 5 seconds but -r 25 overrides this so that the frame rate is 25 fps
        // -shortest clips the audio to be the same length as the video
        // -c:v selects the codecs for the video to be libx264 which is a common encoder i.e. -(codecs):(video)
        String[] cmd = {"ffmpeg", "-framerate", "1/5", "-r", "25", "-loop", "1", "-i", imagePath, "-i", audioPath, "-c:v", "libx264", "-t", duration.toString(), "-pix_fmt", "yuv420p", "-vf", "scale=1080:720", "-shortest", outFile};
//        String cmd = String.format("ffmpeg -framerate 1/5 -r 25 -loop 1 -i %s -i %s -c:v libx264 -t %s -pix_fmt yuv420p -vf scale=1080:720 -shortest %s", imagePath, audioPath, 2, outFile);
        System.out.println(String.join(" ", cmd));
        System.out.println("Creating startup tapestry...");
        executeFfmpeg(cmd);
        System.out.println("Tapestry created");
    }

    public String serverStatusMessage() {
        return "<div style='display:flex;padding-top:3em;justify-content:center;font-size:2em;'>" +
            "<h2 style=font-family:'Arial'>Server is up</h2>" +
        "</div>";
    }
}
