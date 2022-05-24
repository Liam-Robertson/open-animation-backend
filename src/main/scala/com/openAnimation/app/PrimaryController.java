package com.openAnimation.app;

import com.openAnimation.app.models.Commentary;
import com.openAnimation.app.models.Snippet;
import com.openAnimation.app.services.StartupService;
import java.io.IOException;
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
    public void addSnippetToTapestry(@RequestParam Snippet snippet) {
        primaryService.addSnippetToTapestry(snippet);
    }

    @PostMapping("/addCommentary")
    public void addCommentary(@RequestBody Commentary commentary) {
        primaryService.persistCommentary(commentary);
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
