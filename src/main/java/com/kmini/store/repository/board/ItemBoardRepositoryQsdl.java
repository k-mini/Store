package com.kmini.store.repository.board;

import com.kmini.store.domain.ItemBoard;
import com.kmini.store.dto.response.ItemBoardDto.ItemBoardRespDetailDto;

import java.util.Optional;

public interface ItemBoardRepositoryQsdl {

    Optional<ItemBoard> findByIdWithUserAndComments(Long id);

    Optional<ItemBoardRespDetailDto> findDetailById(Long id);
}
