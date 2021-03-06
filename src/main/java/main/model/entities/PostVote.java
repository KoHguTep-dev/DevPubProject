package main.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter @Setter
@Entity
@Table(name = "post_votes")
public class PostVote {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post postId;

    @Column(nullable = false)
    private Date time;

    @Column(nullable = false)
    private byte value;

    public PostVote() {}

    public PostVote(Post post, User user) {
        setUserId(user);
        setPostId(post);
        setTime(new Date());
    }
}
