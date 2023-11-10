package com.kmini.store.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Comment extends BaseTime{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "CommentToUsers"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID", foreignKey = @ForeignKey(name = "CommentToBoard"))
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
//    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "TOP_COMMENT_ID", foreignKey = @ForeignKey(name = "SubToTop"))
    private Comment topComment;

    @OneToMany(mappedBy = "topComment", cascade = CascadeType.REMOVE)
    private List<Comment> subComments = new ArrayList<>();

    private String content;

    public Comment(User user, Board board, Comment topComment, String content) {
        this.user = user;
        this.board = board;
        this.topComment = topComment;
        this.content = content;
    }
}
