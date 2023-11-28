package com.kmini.store.repository.board;

import com.kmini.store.domain.Board;
import com.kmini.store.domain.ItemBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemBoardRepository extends JpaRepository<ItemBoard, Long>, ItemBoardRepositoryQsdl {

    @Query("select i from ItemBoard i left join fetch i.trades where i.id = :boardId")
    Optional<ItemBoard> findByIdFetchJoinTrade(@Param("boardId") Long boardId);

    @Query("select i from ItemBoard i join fetch i.user left join fetch i.comments where i.id = :id")
    Optional<ItemBoard> findByIdWithUserAndComments2(@Param("id") Long id);
}
