package com.kmini.store.repository.board;

import com.kmini.store.domain.CommunityBoard;

import java.util.Optional;

public interface CommunityBoardRepositoryCustom {

    Optional<CommunityBoard> findByIdWithFetchJoin(Long id);
}
