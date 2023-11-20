package com.kmini.store.repository.board;

import com.kmini.store.domain.ItemBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemBoardRepository extends JpaRepository<ItemBoard, Long>, ItemBoardRepositoryQsdl {

}
