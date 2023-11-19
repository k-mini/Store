package com.kmini.store.service;

import com.kmini.store.domain.Board;
import com.kmini.store.domain.Trade;
import com.kmini.store.domain.User;
import com.kmini.store.domain.type.TradeStatus;
import com.kmini.store.dto.request.TradeDto.TradeReqDto;
import com.kmini.store.dto.response.TradeDto.TradeRespDto;
import com.kmini.store.repository.TradeRepository;
import com.kmini.store.repository.UserRepository;
import com.kmini.store.repository.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static com.kmini.store.domain.type.TradeStatus.*;

@Service
@RequiredArgsConstructor
public class TradeService {

    private final TradeRepository tradeRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public TradeRespDto beginTrade(TradeReqDto tradeReqDto) {
        
        // 거래 가능 한지 확인
        boolean isPossible = isTradePossible(tradeReqDto.getBoardId());

        if ( !isPossible ) {
            throw new IllegalStateException("신청이 불가능 합니다.");
        }

        // buyer 생성
        User buyer = userRepository.getReferenceById(tradeReqDto.getBuyerId());

        // board 생성
        Board board = boardRepository.getReferenceById(tradeReqDto.getBoardId());

        // 거래 트랜잭션 생성
        Trade trade = new Trade();
        trade.setTradeStatus(DEALING);
        trade.setBuyer(buyer);
        trade.setBoard(board);

        // 거래 등록
        Trade savedTrade;
        try {
            savedTrade = tradeRepository.save(trade);
        } catch (EntityNotFoundException e) {
            throw new IllegalArgumentException("엔티티를 찾을 수 없습니다. 잘못된 요청입니다.",e);
        }

        return TradeRespDto.toDto(savedTrade);
    }

    // 거래 가능 여부 판별
    public boolean isTradePossible(Long boardId) {
        Trade latestTrade = tradeRepository.getLatestTrade(boardId)
                .orElse(null);

//         거래 이력이 없음
        if (latestTrade == null) {
            return true;
        }

        TradeStatus latestStatus = latestTrade.getTradeStatus();
//         WAIT(수락 대기중) 이거나 DEALING(거래 중), COMPLETE(거래 완료)면  거래 불가
        if (latestStatus == WAIT || latestStatus == DEALING || latestStatus == COMPLETE) {
            return false;
        }
        // 거래 취소된 상태면 거래 가능
        if (latestStatus == CANCEL) {
            return true;
        }

        throw new IllegalStateException("등록되지 않은 거래 상태입니다.");
    }
}
