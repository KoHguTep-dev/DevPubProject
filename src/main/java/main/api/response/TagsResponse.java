package main.api.response;

import main.model.ModerationStatus;
import main.model.Post;
import main.repository.PostsRepository;
import main.repository.TagsRepository;
import main.service.TagWithWeight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TagsResponse {

    @Autowired
    TagsRepository tagsRepository;

    @Autowired
    TagWithWeight tagWithWeight;

    @Autowired
    PostsRepository postsRepository;

    private List<TagWithWeight> tags = new ArrayList<>();
    private double k;
    private int count;

    public List<TagWithWeight> getTags() {
        if (tags.isEmpty()) {
            setCount();
            if (k == 0) {
                k = tagWithWeight.computeK(k, count);
            }
            tagsRepository.findAll().forEach(tag -> {
                tagWithWeight.setName(tag.getName());
                tagWithWeight.setWeight(tag.getId(), k, count);

                TagWithWeight tagW = new TagWithWeight();
                tagW.getCopy(tagWithWeight);
                tags.add(tagW);
            });
        }
        return tags;
    }

    public List<TagWithWeight> getTags(String query) {
        tags.clear();
        TagWithWeight weight = new TagWithWeight();
        weight.setName(tagsRepository.findByName(query).getName());
        weight.setWeight(1.0);
        tags.add(weight);
        return tags;
    }

    public void setTags(List<TagWithWeight> tags) {
        this.tags = tags;
    }

    public void setCount() {
        for (Post post : postsRepository.findAll()) {
            if (post.isActive() && post.getModerationStatus() == ModerationStatus.ACCEPTED) {
                count++;
            }
        }
    }

}
