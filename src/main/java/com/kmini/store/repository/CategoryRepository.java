package com.kmini.store.repository;

import com.kmini.store.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

//    Optional<Category> findByCategoryType(CategoryType categoryType);

    Optional<Category> findByCategoryName(String categoryName);

    @Query("select c from Category c join fetch c.childCategories where c.superCategory is null")
    List<Category> findSuperCategories();
}
