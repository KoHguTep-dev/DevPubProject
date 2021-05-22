package main.controller;

import main.api.response.PostsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller()
@RequestMapping("/api/post")
public class ApiPostController {

    @Autowired
    private PostsResponse postsResponse;

    @GetMapping("")
    @ResponseBody
    private PostsResponse getPosts(@RequestParam("mode") String mode) {
        postsResponse.recent();
        if (mode.equals("popular")) {
            postsResponse.popular();
        }
        if (mode.equals("best")) {
            postsResponse.best();
        }
        if (mode.equals("early")) {
            postsResponse.early();
        }
        return postsResponse;
    }
}
