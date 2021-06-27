package main.service;

import main.api.response.TagsResponse;
import main.model.dto.TagWithWeight;
import main.model.entities.Tag;
import main.repository.PostsRepository;
import main.repository.TagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TagService {

    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private TagsRepository tagsRepository;

    public TagsResponse tagsResponse(String query) {
        Date date = new Date();
        int count = postsRepository.getCountAllAvailable(date);
        Map<Tag, Integer> tags = new HashMap<>();

        TagsResponse tagsResponse = new TagsResponse(new TagWithWeight());
        tagsResponse.setCount(count);

        if (query == null || query.equals("")) {
            put(tagsRepository.getActualTags(date), tags, date);
            tagsResponse.setTags(tags);
            return tagsResponse;
        } else put(tagsRepository.searchByName(query, date), tags, date);
        tagsResponse.setTags(tags);
        return tagsResponse;
    }

    private void put(List<Tag> tagList, Map<Tag, Integer> tags, Date date) {
        for (Tag t : tagList) {
            int c = tagsRepository.getActualPostsByTag(t, date);
            if (c > 0) {
                tags.put(t, c);
            }
        }
    }

}
