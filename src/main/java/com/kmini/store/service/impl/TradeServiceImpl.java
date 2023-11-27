package com.kmini.store.service.impl;

import com.kmini.store.domain.ItemBoard;
import com.kmini.store.domain.Trade;
import com.kmini.store.domain.User;
import com.kmini.store.domain.type.TradeStatus;
import com.kmini.store.dto.request.TradeDto.TradeHistoryReqDto;
import com.kmini.store.dto.response.TradeDto.TradeHistoryRespDto;
import com.kmini.store.repository.TradeRepository;
import com.kmini.store.repository.UserRepository;
import com.kmini.store.repository.board.ItemBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.kmini.store.domain.type.TradeStatus.*;

@Service
@RequiredArgsConstructor
public class TradeServiceImpl {

    private final TradeRepository tradeRepository;
    private final UserRepository userRepository;
    private final ItemBoardRepository itemBoardRepository;

    @Transactional
    public void registerTrade(Long boardId) {

        ItemBoard board = itemBoardRepository.findByIdFetchJoinTrade(boardId)
                .orElseThrow(()-> new IllegalArgumentException("게시물을 찾을 수 없습니다."));

        // 해당 게시물이 종료되었거나 취소되었거나 거래중인가?
        if (board.hasCompletedTrade() || board.hasCanceledTrade() || board.hasDealingTrade()) {
            throw new IllegalStateException("거래 신청 가능한 게시물이 아닙니다.");
        }

        // buyer 생성
        User buyer = User.getSecurityContextUser();

        // 거래 생성
        Trade trade = new Trade();
        trade.setTradeStatus(WAIT);
        trade.setBuyer(buyer);
        trade.setBoard(board);

        // 거래 등록
        tradeRepository.save(trade);
    }

    // 거래중인 목록
    @Transactional
    public Page<TradeHistoryRespDto> viewTradeHistory(TradeHistoryReqDto tradeHistoryReqDto, Pageable pageable) {

        Page<Trade> histories = tradeRepository.findByHistoryByUserIdAndKeyword(tradeHistoryReqDto, pageable);

        return histories.map(TradeHistoryRespDto::toDto);
    }

    // 거래 수락
    @Transactional
    public void acceptTrade(Long tradeId) {
        Trade trade = tradeRepository.findByIdFetchJoin(tradeId).orElseThrow(()-> new IllegalArgumentException("거래를 찾을 수 없습니다."));

        // 거래 가능한지 체크
        checkAcceptAvailable(trade);

        trade.setTradeStatus(DEALING);
    }

    // 거래 거절
    @Transactional
    public void denyTrade(Long tradeId) {

        Trade trade = tradeRepository.findByIdFetchJoin(tradeId).orElseThrow(()-> new IllegalArgumentException("거래를 찾을 수 없습니다."));

        // 거절 가능한지 체크
        checkDenyAvailable(trade);
        trade.setTradeStatus(DENY);
    }

    // 거래 완료
    @Transactional
    public void completeTrade(Long tradeId) {
        Trade trade = tradeRepository.findByIdFetchJoin(tradeId).orElseThrow(()-> new IllegalArgumentException("거래를 찾을 수 없습니다."));

        // 거래 완료 가능한지 체크
        checkCompleteAvailable(trade);
        trade.setTradeStatus(COMPLETE);
    }

    // 거래 취소
    @Transactional
    public void cancelTrade(Long tradeId) {
        Trade trade = tradeRepository.findByIdFetchJoin(tradeId).orElseThrow(()-> new IllegalArgumentException("거래를 찾을 수 없습니다."));

        // 거래 취소 가능한지 체크
        checkCancelAvailable(trade);
        trade.setTradeStatus(CANCEL);
    }

    // 거래 가능 여부 판별
    public boolean checkRegisterTradeAvailable(Long boardId) {
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

    private void checkCompleteAvailable(Trade trade) {
        TradeStatus status = trade.getTradeStatus();

        // 상관 없는 사용자 거부
        trade.checkBuyerOrSellerIsMe();

        confirmCorrectTradeStatus(trade.getTradeStatus(), COMPLETE);
    }

    private void checkCancelAvailable(Trade trade) {
        TradeStatus status = trade.getTradeStatus();

        trade.checkBuyerOrSellerIsMe();

        confirmCorrectTradeStatus(trade.getTradeStatus(), CANCEL);
    }

    private void checkAcceptAvailable(Trade trade) {
        TradeStatus status = trade.getTradeStatus();

        trade.checkBuyerOrSellerIsMe();

        confirmCorrectTradeStatus(trade.getTradeStatus(), DEALING);
    }

    private void checkDenyAvailable(Trade trade) {
        // 상관 없는 사용자 거부
        trade.checkBuyerOrSellerIsMe();

        // 현재 상태에서 다음 상태로 이동가능?
        confirmCorrectTradeStatus(trade.getTradeStatus(), DENY);
    }

    private void confirmCorrectTradeStatus(TradeStatus nowStatus, TradeStatus nextStatus) {

        // 수락대기중은 거절 또는 수락만 가능
        if (nowStatus.equals(WAIT)) {
            if ( !nextStatus.equals(DENY) && !nextStatus.equals(DEALING)) {
                throw new IllegalStateException("수락 대기중 상태는 거래 거절 또는 거래 진행중 상태로만 이동할 수 있습니다.");
            }
        }
        
        // 거래 진행중은 완료 또는 취소만 가능
        if (nowStatus.equals(DEALING)) {
            if ( !nextStatus.equals(COMPLETE) && !nextStatus.equals(CANCEL)) {
                throw new IllegalStateException("거래 진행중 상태는 거래완료 또는 취소 상태로만 이동할 수 있습니다.");
            }
        }
        
        // 거래 거절은 수락 대기중만 가능
        if (nowStatus.equals(DENY)) {
            if (!nextStatus.equals(WAIT)) {
                throw new IllegalStateException("거래 거절은 수락 대기중으로만 이동할 수 있습니다.");
            }
        }

        if (nowStatus.equals(COMPLETE)) {
            throw new IllegalStateException("거래가 이미 완료되었습니다.");
        }

        if (nowStatus.equals(CANCEL)) {
            throw new IllegalStateException("취소된 상품입니다.");
        }
    }

}
