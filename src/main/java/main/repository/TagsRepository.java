package main.repository;

import main.model.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TagsRepository extends JpaRepository<Tag, Integer> {
    Tag findByName(String tagName);

    @Query("select t from Tag t join TagToPost tp on t.id = tp.tagId join Post p on tp.postId = p.id where p.isActive = true and p.moderationStatus = 'ACCEPTED' and p.time <= :time")
    List<Tag> getActualTags(Date time);

    @Query("select t from Tag t join TagToPost tp on t.id = tp.tagId join Post p on tp.postId = p.id where t.name like %:query% and p.isActive = true and p.moderationStatus = 'ACCEPTED' and p.time <= :time")
    List<Tag> searchByName(String query, Date time);

    @Query("select count(*) from Tag t join TagToPost tp on t.id = tp.tagId join Post p on tp.postId = p.id where t = :tag and p.isActive = true and p.moderationStatus = 'ACCEPTED' and p.time <= :time")
    int getActualPostsByTag(Tag tag, Date time);
}
