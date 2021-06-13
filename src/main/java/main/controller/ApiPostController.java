package main.controller;

import main.api.response.PostResponse;
import main.model.dto.PostView;
import main.service.PostService;
import main.service.SessionConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller()
@RequestMapping("/api/post")
public class ApiPostController {

    @Autowired
    private PostService postService;
    @Autowired
    SessionConfig sessionConfig;

    @GetMapping("")
    @ResponseBody
    private ResponseEntity<PostResponse> getPosts(
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit,
            @RequestParam("mode") String mode) {
        return ResponseEntity.ok(postService.postResponse(offset, limit, mode));
    }

    @GetMapping("/search")
    @ResponseBody
    private ResponseEntity<PostResponse> getSearch(
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit,
            @RequestParam("query") String query) {
        return ResponseEntity.ok(postService.search(offset, limit, query));
    }

    @GetMapping("/byDate")
    @ResponseBody
    private ResponseEntity<PostResponse> getByDate(
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit,
            @RequestParam("date") String date) {
        return ResponseEntity.ok(postService.byDate(offset, limit, date));
    }

    @GetMapping("/byTag")
    @ResponseBody
    private ResponseEntity<PostResponse> getByTag(
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit,
            @RequestParam("tag") String tag) {
        return ResponseEntity.ok(postService.byTag(offset, limit, tag));
    }

    @GetMapping("/{ID}")
    @ResponseBody
    private ResponseEntity<PostView> getPost(@PathVariable(name = "ID") int id) {
        PostView postView = postService.getPost(id, sessionConfig);
        if (postView == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(postView);
    }

    @GetMapping("/my")
    @ResponseBody
    private ResponseEntity<PostResponse> getMy(
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit,
            @RequestParam("status") String status) {
        return ResponseEntity.ok(postService.getMyPosts(offset, limit, status, sessionConfig));
    }

}
