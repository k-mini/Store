package com.kmini.store.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "CommentToUsers"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID", foreignKey = @ForeignKey(name = "CommentToBoard"))
    private Board board;

    private String content;

}
