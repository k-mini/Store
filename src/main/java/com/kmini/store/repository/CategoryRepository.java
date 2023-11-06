package com.kmini.store.repository;

import com.kmini.store.domain.Category;
import com.kmini.store.domain.type.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByCategoryType(CategoryType type);


}