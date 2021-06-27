package main.controller;

import main.api.request.CommentRequest;
import main.api.request.ModerationRequest;
import main.api.request.ProfileRequest;
import main.api.response.*;
import main.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @Autowired
    private PostService postService;
    @Autowired
    private AuthService authService;

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

    @PostMapping("/comment")
    @ResponseBody
    private ResponseEntity<CommentResponse> addComment(@RequestBody CommentRequest request) {
        CommentResponse response = postService.addComment(request);
        if (response.getErrors().isEmpty()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(new CommentResponse());
    }

    @GetMapping("/statistics/my")
    @ResponseBody
    private ResponseEntity<StatisticsResponse> getStat() {
        StatisticsResponse response = postService.statisticsByUser();
        if (response == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/statistics/all")
    @ResponseBody
    private ResponseEntity<StatisticsResponse> getAllStat() {
        StatisticsResponse response = postService.statistics(settingsService.settingsResponse());
        if (response == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/image")
    @ResponseBody
    private ResponseEntity uploadImage(@RequestParam("image") MultipartFile image) {
        String result = postService.uploadImage(image);
        if (result != null) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.badRequest().body(new ImageResponse());
    }

    @PostMapping(value = "/profile/my", consumes = "multipart/form-data")
    @ResponseBody
    private ResponseEntity<ProfileResponse> editProfile(
            @RequestParam(value = "photo") MultipartFile photo,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "email") String email,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "removePhoto") int removePhoto) {
        return ResponseEntity.ok(authService.editProfile(photo, name, email, password, removePhoto));
    }

    @PostMapping(value = "/profile/my", consumes = "application/json")
    @ResponseBody
    private ResponseEntity<ProfileResponse> editProfile(@RequestBody ProfileRequest request) {
        return ResponseEntity.ok(authService.editProfile(request));
    }

    @PostMapping("/moderation")
    @ResponseBody
    private ResponseEntity<Boolean> moderationPost(@RequestBody ModerationRequest request) {
        return ResponseEntity.ok(postService.moderationPost(request));
    }

    @PutMapping("/settings")
    @ResponseBody
    private ResponseEntity setSettings(@RequestBody SettingsResponse request) {
        settingsService.setSettings(request);
        return ResponseEntity.ok(null);
    }

}
