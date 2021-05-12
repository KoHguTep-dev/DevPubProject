package main.controller;

import main.api.response.InitResponse;
import main.api.response.SettingsResponse;
import main.api.response.TagsResponse;
import main.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiGeneralController {

    @Autowired
    private InitResponse initResponse;
    @Autowired
    private SettingsService settingsService;
    @Autowired
    private TagsResponse tagsResponse;

    @GetMapping("/api/init")
    private InitResponse init() {
        return initResponse;
    }

    @GetMapping("/api/settings")
    private SettingsResponse settings() {
        return settingsService.getGlobalSettings();
    }

    @GetMapping("/api/tag")
    private TagsResponse getTags() {
        return tagsResponse;
    }

}
