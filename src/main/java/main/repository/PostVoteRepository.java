package main.repository;

import main.model.entities.Post;
import main.model.entities.PostVote;
import main.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostVoteRepository extends JpaRepository<PostVote, Integer> {
    @Query("select v from PostVote v where v.postId = :postId and v.userId = :userId and v.value = :value")
    Optional<PostVote> findVote(Post postId, User userId, byte value);

    @Query("select count(*) from PostVote v where v.userId = :userId and v.value = :value")
    int getVoteCountByUser(User userId, byte value);

    @Query("select count(*) from PostVote v where v.value = :value")
    int getVoteCount(byte value);
}
