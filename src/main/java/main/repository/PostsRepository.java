package main.repository;

import main.model.entities.Post;
import main.model.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Repository
public interface PostsRepository extends JpaRepository<Post, Integer> {
    @Modifying()
    @Query("update Post p set p.viewCount = p.viewCount + 1 where p.id = :id")
    @Transactional
    void addViewCount(@Param("id") int id);

    @Query("select p from Post p where p.isActive = true and p.moderationStatus = 'ACCEPTED' and p.time <= :time")
    Page<Post> findAvailablePosts(Date time, Pageable pageable);

    @Query("select p from Post p where p.isActive = true and p.moderationStatus = 'ACCEPTED' and p.time <= :time")
    Page<Post> findAvailablePostsBest(Date time, Pageable pageable);

    @Query("select count(*) from Post p where p.isActive = true and p.moderationStatus = 'ACCEPTED' and p.time <= :time")
    int getCountAllAvailable(Date time);

    @Query("select count(*) from Post p where p.isActive = true and p.moderationStatus = 'NEW'")
    int getCountForModeration();

    @Query("select p from Post p where p.isActive = true and p.moderationStatus = 'ACCEPTED' and p.time <= :time and title like %:query% or text like %:query%")
    Page<Post> findByQuery(Date time, String query, Pageable pageable);

    @Query("select p from Post p where p.isActive = true and p.moderationStatus = 'ACCEPTED' and p.time <= :time and date(time) = :date")
    Page<Post> findByDate(Date time, Date date, Pageable pageable);

    @Query("select time from Post p where p.isActive = true and p.moderationStatus = 'ACCEPTED' and p.time <= :time")
    List<Date> getAllPostTime(Date time);

    @Query("select p from Post p where p.user = :user and p.isActive = false")
    Page<Post> findByUserInactive(User user, Pageable pageable);

    @Query("select p from Post p where p.user = :user and p.isActive = true and p.moderationStatus = 'NEW'")
    Page<Post> findByUserPending(User user, Pageable pageable);

    @Query("select p from Post p where p.user = :user and p.isActive = true and p.moderationStatus = 'DECLINED'")
    Page<Post> findByUserDeclined(User user, Pageable pageable);

    @Query("select p from Post p where p.user = :user and p.isActive = true and p.moderationStatus = 'ACCEPTED'")
    Page<Post> findByUserPublished(User user, Pageable pageable);

    @Query("select p from Post p where p.isActive = true and p.moderationStatus = 'NEW'")
    Page<Post> findByModeratorNew(Pageable pageable);

    @Query("select p from Post p where p.moderatorId = :id and p.isActive = true and p.moderationStatus = 'DECLINED'")
    Page<Post> findByModeratorDeclined(int id, Pageable pageable);

    @Query("select p from Post p where p.moderatorId = :id and p.isActive = true and p.moderationStatus = 'ACCEPTED'")
    Page<Post> findByModeratorAccepted(int id, Pageable pageable);

    @Query("select count(*) from Post p where p.user = :user and p.isActive = true and p.moderationStatus = 'ACCEPTED'")
    int getPostCountByUser(User user);

    @Query("select sum(p.viewCount) from Post p where p.user = :user and p.isActive = true and p.moderationStatus = 'ACCEPTED'")
    int getViewCountByUser(User user);

    @Query("select min(p.time) from Post p where p.user = :user and p.isActive = true and p.moderationStatus = 'ACCEPTED'")
    Timestamp findFirstByUser(User user);

    @Query("select sum(p.viewCount) from Post p where p.isActive = true and p.moderationStatus = 'ACCEPTED'")
    int getViewCount();

    @Query("select min(p.time) from Post p where p.isActive = true and p.moderationStatus = 'ACCEPTED'")
    Timestamp findFirst();

}
