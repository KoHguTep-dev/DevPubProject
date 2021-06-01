package main.controller;

import main.api.response.PostsResponse;
import main.service.PostView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller()
@RequestMapping("/api/post")
public class ApiPostController {

    @Autowired
    private PostsResponse postsResponse;

    @Autowired
    private PostView postView;

    @GetMapping("")
    @ResponseBody
    private PostsResponse getPosts(
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit,
            @RequestParam("mode") String mode) {
        postsResponse.addPosts(offset, limit, mode);
        return postsResponse;
    }

    @GetMapping("/search")
    @ResponseBody
    private PostsResponse getSearch(
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit,
            @RequestParam("query") String query) {
        postsResponse.search(offset, limit, query);
        return postsResponse;
    }

    @GetMapping("/byDate")
    @ResponseBody
    private PostsResponse getByDate(
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit,
            @RequestParam("date") String date) {
        postsResponse.byDate(offset, limit, date);
        return postsResponse;
    }

    @GetMapping("/byTag")
    @ResponseBody
    private PostsResponse getByTag(
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit,
            @RequestParam("tag") String tag) {
        postsResponse.byTag(offset, limit, tag);
        return postsResponse;
    }

    @GetMapping("/{ID}")
    @ResponseBody
    private ResponseEntity getPost(@PathVariable(name = "ID") int id) {
        postView.get(id);
        if (postView == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity(postView, HttpStatus.OK);
    }

}
