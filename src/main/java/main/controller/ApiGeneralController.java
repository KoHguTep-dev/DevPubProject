package main.controller;

import main.api.response.CalendarResponse;
import main.api.response.InitResponse;
import main.api.response.SettingsResponse;
import main.api.response.tags.TagsResponse;
import main.service.CalendarService;
import main.service.InitService;
import main.service.SettingsService;
import main.service.TagService;
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
    private TagService tagService;
    @Autowired
    private CalendarService calendarService;

    @GetMapping("/init")
    private ResponseEntity<InitResponse> init() {
        return ResponseEntity.ok(initService.initResponse());
    }

    @GetMapping("/settings")
    private ResponseEntity<SettingsResponse> settings() {
        return ResponseEntity.ok(settingsService.settingsResponse());
    }

    @GetMapping("/tag")
    private ResponseEntity<TagsResponse> getTags(@RequestParam(required = false, name = "query") String query) {
        return ResponseEntity.ok(tagService.tagsResponse(query));
    }

    @GetMapping("/calendar")
    private ResponseEntity<CalendarResponse> getCalendar() {
        return ResponseEntity.ok(calendarService.calendarResponse());
    }

}
