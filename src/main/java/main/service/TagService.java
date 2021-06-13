package main.service;

import main.model.dto.TagWithWeight;
import main.api.response.TagsResponse;
import main.repository.PostsRepository;
import main.repository.TagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagService {

    @Autowired
    private TagsRepository tagsRepository;
    @Autowired
    private PostsRepository postsRepository;

    public TagsResponse tagsResponse(String query) {
        if (TagsResponse.tagList == null && TagsResponse.posts == null) {
            TagsResponse.tagList = tagsRepository.findAll();
            TagsResponse.posts = postsRepository.findAll();
        }
        TagsResponse tagsResponse = new TagsResponse(tagWithWeight());
        if (query != null) {
            tagsResponse.getTags(query);
        }
        return tagsResponse;
    }

    public TagWithWeight tagWithWeight() {
        if (TagWithWeight.tags == null) {
            TagWithWeight.tags = tagsRepository.findAll();
        }
        return new TagWithWeight();
    }
}
