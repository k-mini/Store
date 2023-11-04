package com.kmini.store.domain;

import com.kmini.store.domain.type.CategoryType;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;

    @ManyToOne
    private Category parent;

    public Category(CategoryType categoryType) {
        this.categoryType = categoryType;
    }

    public Category(CategoryType categoryType, Category parent) {
        this.categoryType = categoryType;
        this.parent = parent;
    }
}
