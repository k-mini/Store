package com.kmini.store.domain;


import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn // 자식 객체 구분 컬럼 기본 값 "DType"
public abstract class Board extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "BoardToUser"))
    private User user;

    @OneToMany(mappedBy = "board")
    private List<Comment> comments = new ArrayList<>();

    private String thumbnail;
    private String title;
    private String content;
    private long views;

    @OneToMany(mappedBy = "board")
    public List<BoardCategory> boardCategories= new ArrayList<>();

    @Column(insertable = false, updatable = false)
    private String dtype;

    @Transient
    private MultipartFile file;

    @Transient
    private String categoryName;
    @Transient
    private String subCategoryName;

    public Board(User user, String content) {
        this.user = user;
        this.content = content;
    }

    public Category getCategory() {
        return getBoardCategories()
                .stream()
                .map(BoardCategory::getCategory)
                .filter(Category::isSuperCategory)
                .findAny().get();
    }

    public Category getSubCategory() {
        return getBoardCategories()
                .stream()
                .map(BoardCategory::getCategory)
                .filter(Category::isSubCategory)
                .findAny().get();
    }
}
