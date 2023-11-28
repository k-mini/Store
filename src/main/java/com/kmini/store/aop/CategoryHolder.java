package com.kmini.store.aop;

import com.kmini.store.domain.type.CategoryType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
@Setter
@Component
public class CategoryHolder {
    private MultiValueMap<CategoryType, CategoryType> map = new LinkedMultiValueMap<>();
}
