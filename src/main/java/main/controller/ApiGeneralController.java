package main.controller;

import main.api.response.InitResponse;
import main.api.response.SettingsResponse;
import main.api.response.TagsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiGeneralController {

    @Autowired
    private InitResponse initResponse;
    @Autowired
    private SettingsResponse settingsResponse;
    @Autowired
    private TagsResponse tagsResponse;

    @GetMapping("/api/init")
    private InitResponse init() {
        return initResponse;
    }

    @GetMapping("/api/settings")
    private SettingsResponse settings() {
        settingsResponse.getSettings();
        return settingsResponse;
    }

    @GetMapping("/api/tag")
    private TagsResponse getTags(@RequestParam(required = false, name = "query") String query) {
        if (query != null) {
            tagsResponse.getTags(query);
        }
        return tagsResponse;
    }

}
