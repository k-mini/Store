package com.kmini.store.domain.board.item.repository;

import com.kmini.store.domain.board.item.dto.ItemBoardReponseDto.ItemBoardViewRespDto;

import java.util.Optional;

public interface ItemBoardRepositoryQsdl {

    Optional<ItemBoardViewRespDto> findDetailById(Long id);
}
