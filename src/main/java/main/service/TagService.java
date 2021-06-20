package main.service;

import main.model.dto.TagWithWeight;
import main.api.response.TagsResponse;
import main.model.entities.Tag;
import main.repository.PostsRepository;
import main.repository.TagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TagService {

    @Autowired
    private TagsRepository tagsRepository;
    @Autowired
    private PostsRepository postsRepository;

    public TagsResponse tagsResponse(String query) {
        int count = postsRepository.getCountAllAvailable(new Date());
        List<Tag> tagList = tagsRepository.findAll();

        TagsResponse tagsResponse = new TagsResponse(new TagWithWeight());
        tagsResponse.setCount(count);
        tagsResponse.getTags(tagList);
        if (query != null) {
            tagsResponse.getTags(query);
        }
        return tagsResponse;
    }

}
