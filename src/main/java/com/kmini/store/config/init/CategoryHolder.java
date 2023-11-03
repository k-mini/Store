package com.kmini.store.config.init;

import com.kmini.store.domain.type.BoardType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
@Setter
@Component
public class CategoryHolder {

    private MultiValueMap<BoardType, BoardType> map = new LinkedMultiValueMap<>();
}
