package com.kmini.store.repository.board;

import com.kmini.store.domain.ItemBoard;

import java.util.Optional;

public interface ItemBoardRepositoryCustom {

    Optional<ItemBoard> findByIdWithFetchJoin(Long id);
}
