package main.controller;

import main.api.response.CalendarResponse;
import main.api.response.InitResponse;
import main.api.response.SettingsResponse;
import main.api.response.TagsResponse;
import main.service.InitService;
import main.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    @Autowired
    private InitService initService;
    @Autowired
    private SettingsService settingsService;
    @Autowired
    private TagsResponse tagsResponse;
    @Autowired
    private CalendarResponse calendarResponse;

    @GetMapping("/init")
    private ResponseEntity<InitResponse> init() {
        return ResponseEntity.ok(initService.initResponse());
    }

    @GetMapping("/settings")
    private ResponseEntity<SettingsResponse> settings() {
        return ResponseEntity.ok(settingsService.settingsResponse());
    }

    @GetMapping("/tag")
    private TagsResponse getTags(@RequestParam(required = false, name = "query") String query) {
        if (query != null) {
            tagsResponse.getTags(query);
        }
        return tagsResponse;
    }

    @GetMapping("/calendar")
    private CalendarResponse getCalendar() {
        return calendarResponse;
    }

}
