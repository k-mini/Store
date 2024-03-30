package com.kmini.store.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BoardCategory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_CATEGORY_ID")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    public BoardCategory(Board board, Category category) {
        this.board = board;
        this.category = category;
    }
}
