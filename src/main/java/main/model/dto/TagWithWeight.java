package main.model.dto;

import lombok.Getter;
import lombok.Setter;
import main.model.entities.Tag;

import java.util.*;

@Getter @Setter
public class TagWithWeight {

    private String name;
    private double weight;

    public void setWeight(int tagCount, double k, int count) {
        weight = (double) tagCount / count * k;
    }

    public void getCopy(TagWithWeight tag) {
        name = tag.name;
        weight = tag.weight;
    }

    public double computeK(int count, Map<Tag, Integer> tagList) {
        int value = Collections.max(tagList.values());
        double meta = (double) value / count;
        return 1 / meta;
    }

}
