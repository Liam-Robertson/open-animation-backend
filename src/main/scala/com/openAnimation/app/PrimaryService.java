package com.openAnimation.app;

import com.openAnimation.app.models.Commentary;
import com.openAnimation.app.models.Snippet;
import com.openAnimation.app.repository.CommentaryRepository;
import com.openAnimation.app.services.VideoStitchingService1;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Service
public class PrimaryService {

    @Autowired
    private CommentaryRepository commentaryRepository;
    @Autowired
    private VideoStitchingService1 videoStitchingService;

    public String addSnippetToTapestry(Snippet snippet) {
        String duration = videoStitchingService.getDuration(snippet.getStartTime(), snippet.getEndTime());
        String imageData = snippet.getImage().split("data:image/png;base64,")[1];
        String test = this.getClass().getClassLoader().getResource("images").getPath();
        Integer numFiles = new File(this.getClass().getClassLoader().getResource("images").getPath()).listFiles().length + 1;
        String imagePath = new File(this.getClass().getClassLoader().getResource("images").getPath() + String.format("\\currentSnippet%s.png", numFiles)).getPath();
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
    File[] tapestriesList = new File(this.getClass().getClassLoader().getResource("tapestry").getPath()).listFiles();
    File tapestryFilePath = tapestriesList[tapestriesList.length - 1];
    InputStream videoStreamInput = this.getClass().getClassLoader().getResourceAsStream(String.format("tapestry/%s", tapestryFilePath.getName()));
    byte[] videoByteArr = IOUtils.toByteArray(videoStreamInput);
    return videoByteArr;
    }

}