package com.kmini.store.domain.board.community.repository;

import com.kmini.store.domain.entity.CommunityBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommunityBoardRepository extends JpaRepository<CommunityBoard,Long> {

    @Query("select c from CommunityBoard c join fetch c.user left join fetch c.comments where c.id = :id")
    Optional<CommunityBoard> findByIdFetchJoin(@Param("id") Long id);
}
