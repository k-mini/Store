package com.kmini.store.service;

import com.kmini.store.domain.Board;
import com.kmini.store.domain.Trade;
import com.kmini.store.domain.User;
import com.kmini.store.domain.type.TradeStatus;
import com.kmini.store.dto.request.TradeDto;
import com.kmini.store.dto.request.TradeDto.TradeReqDto;
import com.kmini.store.dto.response.TradeDto.TradeRespDto;
import com.kmini.store.repository.TradeRepository;
import com.kmini.store.repository.UserRepository;
import com.kmini.store.repository.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TradeService {

    private final TradeRepository tradeRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public TradeRespDto beginTrade(TradeReqDto tradeReqDto) {
        
        // 이미 거래중인지 검증

        // buyer 생성
        User buyer = userRepository.getReferenceById(tradeReqDto.getBuyerId());

        // board 생성
        Board board = boardRepository.getReferenceById(tradeReqDto.getBoardId());

        // 거래 트랜잭션 생성
        Trade trade = new Trade();
        trade.setTradeStatus(TradeStatus.DEALING);
        trade.setBuyer(buyer);
        trade.setBoard(board);

        // 거래 등록
        Trade savedTrade = tradeRepository.save(trade);
        return TradeRespDto.toDto(savedTrade);
    }
}
