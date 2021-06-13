package main.repository;

import main.model.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PostsRepository extends JpaRepository<Post, Integer> {
    @Modifying()
    @Query("update Post p set p.viewCount = p.viewCount + 1 where p.id = :id")
    @Transactional
    void addViewCount(@Param("id") int id);
}
