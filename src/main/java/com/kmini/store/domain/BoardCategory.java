package com.kmini.store.domain;

import com.kmini.store.domain.type.CategoryType;
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

    @ManyToOne
    private Board board;

    @ManyToOne
    private Category category;

    public BoardCategory(Board board, Category category) {
        this.board = board;
        this.category = category;
    }
}
