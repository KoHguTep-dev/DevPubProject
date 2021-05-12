package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.service.TagWithWeight;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TagsResponse {

    @JsonProperty("tags")
    List<TagWithWeight> tagsWithWeights = new ArrayList<>();

    public TagsResponse() {
    }

    public TagsResponse(String query) {
        if (query.length() == 0) {
            getAllTags();
        }
    }

    public List<TagWithWeight> getTagsWithWeights() {
        return tagsWithWeights;
    }

    public void setTagsWithWeights(List<TagWithWeight> tagsWithWeights) {
        this.tagsWithWeights = tagsWithWeights;
    }

    public void getAllTags() {
        TagWithWeight tag = new TagWithWeight();
        tag.setName("Java");
        tag.setWeight(1);
        tagsWithWeights.add(tag);
    }
}
