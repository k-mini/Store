package com.kmini.store.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@ToString(exclude = "superCategory")
public class Category extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="CATEGORY_ID")
    private Long id;

    @Column(name="CATEGORY_NAME",unique = true)
    private String categoryName;

    @Column(name="CATEGORY_KO_NAME",unique = true)
    private String categoryKoName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="SUPER_CATEGORY")
    private Category superCategory;

    @OneToMany(mappedBy = "superCategory")
    private List<Category> childCategories = new ArrayList<>();

    public Category(String categoryName, String categoryKoName) {
        this.categoryName = categoryName;
        this.categoryKoName = categoryKoName;
    }

    public Category(String categoryName, String categoryKoName, Category superCategory) {
        this.categoryName = categoryName;
        this.categoryKoName = categoryKoName;
        this.superCategory = superCategory;
    }

    public boolean isSuperCategory() {
        return this.superCategory == null;
    }
}
