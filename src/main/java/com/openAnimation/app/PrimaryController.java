package com.openAnimation.app;

import com.openAnimation.app.models.Commentary;
import com.openAnimation.app.models.Snippet;
import com.openAnimation.app.services.StartupService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
public class PrimaryController {

    @Autowired
    private StartupService startupService;
    @Autowired
    private PrimaryService primaryService;

    @PostMapping("/addSnippetToTapestry")
    public String addSnippetToTapestry(@RequestBody Snippet snippet) throws IOException, InterruptedException {
        return primaryService.addSnippetToTapestry(snippet);
    }

    @PostMapping("/saveCommentary")
    public String addCommentary(@RequestBody String comment) {
        return primaryService.persistCommentary(comment);
    }

    @GetMapping("/getAllCommentary")
    public List<Commentary> getAllCommentary() {
        return primaryService.getAllCommentary();
    }

    @GetMapping("/getTapestry")
    public byte[] getTapestry() throws IOException {
        return primaryService.getTapestry();
    }

    @GetMapping("/")
    public String getServerStatusMessage() {
        return startupService.serverStatusMessage();
    }

}
