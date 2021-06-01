package main.service;

import main.repository.TagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class TagWithWeight {

    @Autowired
    private TagsRepository tagsRepository;

    private String name;
    private double weight;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(int id, double k, int count) {
        int tagCount = tagsRepository.findById(id).get().getPosts().size();
        weight = (double) tagCount / count * k;
    }

    public void setWeight(double d) {
        weight = d;
    }

    public void getCopy(TagWithWeight tag) {
        name = tag.name;
        weight = tag.weight;
    }

    public double computeK(double k, int count) {
        List<Integer> list = new ArrayList<>();
        tagsRepository.findAll().forEach(tag -> {
            int size = tag.getPosts().size();
            list.add(size);
        });
        Collections.reverse(list);
        int value = list.get(0);
        double meta = (double) value / count;
        k = 1 / meta;
        return k;
    }
}
