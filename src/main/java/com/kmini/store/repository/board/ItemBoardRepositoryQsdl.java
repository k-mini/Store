package com.kmini.store.repository.board;

import com.kmini.store.dto.response.ItemBoardDto.ItemBoardViewRespDto;

import java.util.Optional;

public interface ItemBoardRepositoryQsdl {

    Optional<ItemBoardViewRespDto> findDetailById(Long id);
}
