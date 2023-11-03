package com.kmini.store.service;

import com.kmini.store.domain.Board;
import com.kmini.store.domain.ItemBoard;
import com.kmini.store.dto.BoardRespDto;
import com.kmini.store.dto.ItemBoardRespDto;
import com.kmini.store.repository.BoardRepository;
import com.kmini.store.repository.CommunityBoardRepository;
import com.kmini.store.repository.ItemBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    // 게시물 조회 ( 디폴트 : 최신 시간 순으로)
    @Transactional(readOnly = true)
    public Page<BoardRespDto> load(Pageable pageable) {
        Page<Board> rawResult = boardRepository.findAll(pageable);

        return rawResult.map(BoardRespDto::toDto);
    }
}
