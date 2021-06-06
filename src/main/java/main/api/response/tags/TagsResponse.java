package main.api.response.tags;

import lombok.Setter;
import main.model.ModerationStatus;
import main.model.Post;
import main.model.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagsResponse {

    @Setter
    private List<TagWithWeight> tags = new ArrayList<>();
    private double k;
    private int count;

    private TagWithWeight tagWithWeight;
    public static List<Tag> tagList;
    public static List<Post> posts;

    public TagsResponse(TagWithWeight tagWithWeight) {
        this.tagWithWeight = tagWithWeight;
        getTags();
    }

    public List<TagWithWeight> getTags() {
        if (tags.isEmpty()) {
            setCount();
            if (k == 0) {
                k = tagWithWeight.computeK(count);
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

    public void setCount() {
        for (Post post : posts) {
            if (post.isActive() && post.getModerationStatus() == ModerationStatus.ACCEPTED) {
                count++;
            }
        }
    }

}
