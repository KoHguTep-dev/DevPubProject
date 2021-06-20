package main.api.response;

import lombok.Getter;
import lombok.Setter;
import main.model.dto.TagWithWeight;
import main.model.entities.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagsResponse {

    @Getter @Setter
    private List<TagWithWeight> tags = new ArrayList<>();
    private double k;
    @Setter
    private int count;
    private TagWithWeight tagWithWeight;

    public TagsResponse(TagWithWeight tagWithWeight) {
        this.tagWithWeight = tagWithWeight;
    }

    public List<TagWithWeight> getTags(List<Tag> tagList) {
        if (tags.isEmpty()) {
            if (k == 0) {
                k = tagWithWeight.computeK(count, tagList);
            }
            tagList.forEach(tag -> {
                tagWithWeight.setName(tag.getName());
                tagWithWeight.setWeight(tag, k, count);

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
        weight.setName(query);
        weight.setWeight(1.0);
        tags.add(weight);
        return tags;
    }

}
