package com.kmini.store.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn // 자식 객체 구분 컬럼 기본 값 "DType"
public abstract class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "BOARD_ID")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "BoardToUser"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID", foreignKey = @ForeignKey(name = "BoardToCategory"))
    private BoardCategory category;

    private String thumbnail;
    private String content;

    private int boardCount;

    public Board(User user, BoardCategory category, String content) {
        this.user = user;
        this.category = category;
        this.content = content;
    }
}
