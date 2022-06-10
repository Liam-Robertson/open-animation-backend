package com.openAnimation.app;

import com.openAnimation.app.models.Commentary;
import com.openAnimation.app.models.Snippet;
import com.openAnimation.app.repository.CommentaryRepository;
import com.openAnimation.app.services.StartupService;
import com.openAnimation.app.services.VideoStitchingService;
import java.io.FileInputStream;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Service
public class PrimaryService {

    @Autowired
    private CommentaryRepository commentaryRepository;
    @Autowired
    private VideoStitchingService videoStitchingService;
    @Autowired
    private StartupService startupService;

    public String addSnippetToTapestry(Snippet snippet) throws IOException, InterruptedException {
        videoStitchingService.formatTimestamp(snippet);
        String duration = videoStitchingService.getDuration(snippet.getStartTime(), snippet.getEndTime());
        String imageData = snippet.getImage().split("data:image/png;base64,")[1];
        Integer numFiles = new File(this.getClass().getClassLoader().getResource("images").getPath()).listFiles().length + 1;
        String imagePath = new File(this.getClass().getClassLoader().getResource("images").getPath() + String.format("/currentSnippet%s.png", numFiles + 1)).getPath();
        videoStitchingService.saveImageToFilesystem(imageData, imagePath);
        videoStitchingService.createVideoFromImage(duration, imagePath);
        videoStitchingService.stitchSnippetIntoTapestry(snippet.getStartTime(), snippet.getEndTime());
        return "Image saved successfully";
    }

    public String persistCommentary(String comment) {
        Commentary commentary = Commentary.builder().comment(comment).build();
        commentaryRepository.save(commentary);
        return "Comment saved";
    }

    public List<Commentary> getAllCommentary() {
        List<Commentary> commentary = commentaryRepository.findAll();
        return commentary;
    }

    public byte[] getTapestry() throws IOException {
        Integer numOfTapestries = new File(this.getClass().getClassLoader().getResource("tapestry").getPath()).listFiles().length;
        File tapestry = new File(this.getClass().getClassLoader().getResource("tapestry").getPath() + String.format("/tapestry%s.mp4", numOfTapestries));
        InputStream videoStreamInput = new FileInputStream(tapestry);
        byte[] videoByteArr = IOUtils.toByteArray(videoStreamInput);
        return videoByteArr;
    }

}