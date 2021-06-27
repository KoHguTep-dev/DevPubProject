package main.api.response;

import lombok.Getter;
import lombok.Setter;
import main.model.dto.TagWithWeight;
import main.model.entities.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TagsResponse {

    @Getter
    private List<TagWithWeight> tags = new ArrayList<>();
    private double k;
    @Setter
    private int count;
    private TagWithWeight tagWithWeight;

    public TagsResponse(TagWithWeight tagWithWeight) {
        this.tagWithWeight = tagWithWeight;
    }

    public List<TagWithWeight> setTags(Map<Tag, Integer> tagList) {
        if (tags.isEmpty()) {
            if (k == 0) {
                k = tagWithWeight.computeK(count, tagList);
            }
            tagList.forEach((tag, integer) -> {
                tagWithWeight.setName(tag.getName());
                tagWithWeight.setWeight(integer, k, count);

                TagWithWeight tagW = new TagWithWeight();
                tagW.getCopy(tagWithWeight);
                tags.add(tagW);
            });
        }
        return tags;
    }

}
