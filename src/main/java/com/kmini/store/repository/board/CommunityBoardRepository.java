package com.kmini.store.repository.board;

import com.kmini.store.domain.CommunityBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityBoardRepository extends JpaRepository<CommunityBoard,Long> {
}