package main.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.api.request.PostRequest;
import main.api.response.PostResponse;
import main.api.response.PostsResponse;
import main.model.dto.PostView;
import main.service.PostService;
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

    @GetMapping("")
    @ResponseBody
    private ResponseEntity<PostsResponse> getPosts(
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit,
            @RequestParam("mode") String mode) {
        return ResponseEntity.ok(postService.postResponse(offset, limit, mode));
    }

    @GetMapping("/search")
    @ResponseBody
    private ResponseEntity<PostsResponse> getSearch(
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit,
            @RequestParam("query") String query) {
        return ResponseEntity.ok(postService.search(offset, limit, query));
    }

    @GetMapping("/byDate")
    @ResponseBody
    private ResponseEntity<PostsResponse> getByDate(
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit,
            @RequestParam("date") String date) {
        return ResponseEntity.ok(postService.byDate(offset, limit, date));
    }

    @GetMapping("/byTag")
    @ResponseBody
    private ResponseEntity<PostsResponse> getByTag(
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit,
            @RequestParam("tag") String tag) {
        return ResponseEntity.ok(postService.byTag(offset, limit, tag));
    }

    @GetMapping("/{ID}")
    @ResponseBody
    private ResponseEntity<PostView> getPost(@PathVariable(name = "ID") int id) {
        PostView postView = postService.getPost(id);
        if (postView == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(postView);
    }

    @GetMapping("/my")
    @ResponseBody
    private ResponseEntity<PostsResponse> getMy(
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit,
            @RequestParam("status") String status) {
        return ResponseEntity.ok(postService.getMyPosts(offset, limit, status));
    }

    @PostMapping("")
    @ResponseBody
    private ResponseEntity<PostResponse> addPost(@RequestBody PostRequest request) {
        return ResponseEntity.ok(postService.addPost(request));
    }

    @PutMapping("/{ID}")
    @ResponseBody
    private ResponseEntity<PostResponse> putPost(@PathVariable(name = "ID") int id, @RequestBody PostRequest request) {
        return ResponseEntity.ok(postService.putPost(request, id));
    }

    @PostMapping("/like")
    @ResponseBody
    private ResponseEntity<Boolean> like(@RequestBody @JsonProperty("post_id") String postId) {
        return ResponseEntity.ok(postService.like(postId));
    }

    @PostMapping("/dislike")
    @ResponseBody
    private ResponseEntity<Boolean> dislike(@RequestBody @JsonProperty("post_id") String postId) {
        return ResponseEntity.ok(postService.dislike(postId));
    }

    @GetMapping("/moderation")
    @ResponseBody
    private ResponseEntity<PostsResponse> moderationList(
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit,
            @RequestParam("status") String status) {
        return ResponseEntity.ok(postService.moderationList(offset, limit, status));
    }

}
