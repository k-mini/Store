package com.kmini.store.domain;

import com.kmini.store.domain.type.BoardType;
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
    @Column(name = "CATEGORY_ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    @ManyToOne
    private BoardCategory parentCategory;

    public BoardCategory(BoardType boardType) {
        this.boardType = boardType;
    }

    public BoardCategory(BoardType boardType, BoardCategory parentCategory) {
        this.boardType = boardType;
        this.parentCategory = parentCategory;
    }

    public static void valueOf(String categoryName) {

    }
}
