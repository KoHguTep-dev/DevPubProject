package main.api.response.tags;

import lombok.Getter;
import lombok.Setter;
import main.model.Tag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TagWithWeight {

    @Getter @Setter
    private String name;
    @Getter
    private double weight;

    public static List<Tag> tags;

    public void setWeight(Tag tag, double k, int count) {
        int tagCount = tag.getPosts().size();
        weight = (double) tagCount / count * k;
    }

    public void setWeight(double d) {
        weight = d;
    }

    public void getCopy(TagWithWeight tag) {
        name = tag.name;
        weight = tag.weight;
    }

    public double computeK(int count) {
        List<Integer> list = new ArrayList<>();
        tags.forEach(tag -> list.add(tag.getPosts().size()));
        Collections.reverse(list);
        int value = list.get(0);
        double meta = (double) value / count;
        return 1 / meta;
    }
}
