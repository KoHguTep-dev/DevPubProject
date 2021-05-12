package main.controller;

import main.api.response.PostsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller()
@RequestMapping("/api/post")
public class ApiPostController {

    @Autowired
    private PostsResponse postsResponse;

    @GetMapping("")
    @ResponseBody
    private PostsResponse getPosts() {
        return postsResponse;
    }
}
