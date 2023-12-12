package com.kmini.store.service;

import com.kmini.store.domain.*;
import com.kmini.store.domain.type.TradeStatus;
import com.kmini.store.dto.request.TradeDto.SelectUserTradeHistoryReqDto;
import com.kmini.store.dto.response.TradeDto.*;
import com.kmini.store.repository.BoardCategoryRepository;
import com.kmini.store.repository.TradeRepository;
import com.kmini.store.repository.board.ItemBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.kmini.store.domain.type.CompleteFlag.*;
import static com.kmini.store.domain.type.TradeStatus.*;

@Service
@RequiredArgsConstructor
public class TradeService {

    private final TradeRepository tradeRepository;
    private final ItemBoardRepository itemBoardRepository;
    private final BoardCategoryRepository boardCategoryRepository;

    // 거래중인 목록
    @Transactional
    public Page<TradeHistoryRespDto> selectUserTradeHistory(SelectUserTradeHistoryReqDto selectUserTradeHistoryReqDto, Pageable pageable) {

        Page<Trade> histories = tradeRepository.findTradeHistoryByUserIdAndKeyword(selectUserTradeHistoryReqDto, pageable);

        List<Long> boardIds = histories.getContent()
                                       .stream()
                                       .map(trade -> trade.getBoard().getId()).collect(Collectors.toList());

        List<BoardCategory> boardCategories = boardCategoryRepository.findByBoardIds(boardIds);

        Map<Long, Category> superCategoryMap = new HashMap<>();
        Map<Long, Category> subCategoriyMap = new HashMap<>();

        for (BoardCategory boardCategory : boardCategories) {
            Long boardId = boardCategory.getBoard().getId();
            Category category = boardCategory.getCategory();

            if (category.isSuperCategory()) {
                superCategoryMap.put(boardId, category);
            }
            else {
                subCategoriyMap.put(boardId, category);
            }
        }

        return histories.map(trade -> TradeHistoryRespDto.toDto(trade, superCategoryMap, subCategoriyMap));
    }

    @Transactional
    public TradeRegisterRespDto registerTrade(Long boardId) {

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
        trade.setBuyerCompleteFlag(COMPLETE_ABSTAIN);
        trade.setSellerCompleteFlag(COMPLETE_ABSTAIN);

        // 거래 등록
        Trade savedTrade = tradeRepository.save(trade);
        return TradeRegisterRespDto.toDto(savedTrade);
    }

    // 거래 수락
    @Transactional
    public TradeAcceptRespDto acceptTrade(Long tradeId) {
        Trade trade = tradeRepository.findByIdFetchJoin(tradeId).orElseThrow(()-> new IllegalArgumentException("거래를 찾을 수 없습니다."));

        // 거래 가능한지 체크
        checkAcceptAvailable(trade);

        trade.setTradeStatus(DEALING);

        return TradeAcceptRespDto.toDto(trade);
    }

    // 거래 거절
    @Transactional
    public TradeDenyRespDto denyTrade(Long tradeId) {

        Trade trade = tradeRepository.findByIdFetchJoin(tradeId).orElseThrow(()-> new IllegalArgumentException("거래를 찾을 수 없습니다."));

        // 거절 가능한지 체크
        checkDenyAvailable(trade);

        trade.setTradeStatus(DENY);

        return TradeDenyRespDto.toDto(trade);
    }

    // 거래 완료
    @Transactional
    public TradeCompleteRespDto completeTrade(Long tradeId) {
        Trade trade = tradeRepository.findByIdFetchJoin(tradeId).orElseThrow(()-> new IllegalArgumentException("거래를 찾을 수 없습니다."));

        // 거래 완료 가능한지 체크
        checkCompleteAvailable(trade);

        applyComplete(trade);

        return TradeCompleteRespDto.toDto(trade);
    }

    // 거래 취소
    @Transactional
    public TradeCancelRespDto cancelTrade(Long tradeId) {
        Trade trade = tradeRepository.findByIdFetchJoin(tradeId).orElseThrow(()-> new IllegalArgumentException("거래를 찾을 수 없습니다."));

        // 거래 취소 가능한지 체크
        checkCancelAvailable(trade);

        applyCancel(trade);

        return TradeCancelRespDto.toDto(trade);
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
        checkBuyerOrSeller(trade);

        checkNextTradeStatus(trade.getTradeStatus(), COMPLETE);
    }
    private void checkCancelAvailable(Trade trade) {
        TradeStatus status = trade.getTradeStatus();

        // 상관 없는 사용자 거부
        checkBuyerOrSeller(trade);

        checkNextTradeStatus(trade.getTradeStatus(), CANCEL);
    }

    private void checkAcceptAvailable(Trade trade) {
        TradeStatus status = trade.getTradeStatus();

        // 상관 없는 사용자 거부
        checkSeller(trade);

        checkNextTradeStatus(trade.getTradeStatus(), DEALING);
    }

    private void checkDenyAvailable(Trade trade) {
        // 상관 없는 사용자 거부
        checkSeller(trade);

        // 현재 상태에서 다음 상태로 이동가능?
        checkNextTradeStatus(trade.getTradeStatus(), DENY);
    }

    private void checkNextTradeStatus(TradeStatus nowStatus, TradeStatus nextStatus) {

        // 수락대기중은 거절 또는 수락만 가능
        if (nowStatus.equals(WAIT)) {
            if ( !nextStatus.equals(DENY) && !nextStatus.equals(DEALING)) {
                throw new IllegalStateException("수락 대기중 상태는 거래 거절 또는 거래 진행중 상태로만 이동할 수 있습니다.");
            }
        }

        // 거래 진행중은 완료 또는 취소만 가능
        else if (nowStatus.equals(DEALING)) {
            if ( !nextStatus.equals(COMPLETE) && !nextStatus.equals(CANCEL)) {
                throw new IllegalStateException("거래 진행중 상태는 거래완료 또는 취소 상태로만 이동할 수 있습니다.");
            }
        }

        // 거래 거절은 수락 대기중만 가능
        else if (nowStatus.equals(DENY)) {
            if (!nextStatus.equals(WAIT)) {
                throw new IllegalStateException("거래 거절은 수락 대기중으로만 이동할 수 있습니다.");
            }
        }

        else if (nowStatus.equals(COMPLETE)) {
            throw new IllegalStateException("거래가 이미 완료되었습니다.");
        }

        else if (nowStatus.equals(CANCEL)) {
            throw new IllegalStateException("취소된 상품입니다.");
        }
    }

    public void checkBuyer(Trade trade) {
        Long buyerId = trade.getBuyer().getId();
        User user = User.getSecurityContextUser();

        if (!user.getId().equals(buyerId)) {
            throw new IllegalArgumentException("구매자가 아닙니다.");
        }
    }

    public void checkSeller(Trade trade) {
        Long sellerId = trade.getSeller().getId();
        User user = User.getSecurityContextUser();

        if (!user.getId().equals(sellerId)) {
            throw new IllegalArgumentException("판매자가 아닙니다.");
        }
    }

    private void checkBuyerOrSeller(Trade trade) {
        Long buyerId = trade.getBuyer().getId();
        Long sellerId = trade.getSeller().getId();
        User user = User.getSecurityContextUser();

        if (!user.getId().equals(sellerId) && !user.getId().equals(buyerId)) {
            throw new IllegalArgumentException("판매자나 구매자가 아닙니다.");
        }
    }

    private void applyComplete(Trade trade) {
        User user = User.getSecurityContextUser();

        if (user.getId().equals(trade.getBuyer().getId())) {
            if (!trade.getBuyerCompleteFlag().equals(COMPLETE_ABSTAIN)) {
                throw new IllegalStateException("이미 완료 또는 취소 버튼을 누르셨습니다.");
            }
            trade.setBuyerCompleteFlag(COMPLETE_CONFIRM);
        }

        else if (user.getId().equals(trade.getSeller().getId())) {
            if (!trade.getSellerCompleteFlag().equals(COMPLETE_ABSTAIN)) {
                throw new IllegalStateException("이미 완료 또는 취소 버튼을 누르셨습니다.");
            }
            trade.setSellerCompleteFlag(COMPLETE_CONFIRM);
        }

        if ( COMPLETE_CONFIRM.equals(trade.getSellerCompleteFlag()) && COMPLETE_CONFIRM.equals(trade.getBuyerCompleteFlag()) ) {
            trade.setTradeStatus(COMPLETE);
        }
    }

    private void applyCancel(Trade trade) {

        User user = User.getSecurityContextUser();

        if (user.getId().equals(trade.getBuyer().getId())) {
            if (!trade.getBuyerCompleteFlag().equals(COMPLETE_ABSTAIN)) {
                throw new IllegalStateException("이미 완료 또는 취소 버튼을 누르셨습니다.");
            }
            trade.setBuyerCompleteFlag(COMPLETE_DENY);
        }

        else if (user.getId().equals(trade.getSeller().getId())) {
            if (!trade.getSellerCompleteFlag().equals(COMPLETE_ABSTAIN)) {
                throw new IllegalStateException("이미 완료 또는 취소 버튼을 누르셨습니다.");
            }
            trade.setSellerCompleteFlag(COMPLETE_DENY);
        }

        if ( COMPLETE_DENY.equals(trade.getSellerCompleteFlag()) && COMPLETE_DENY.equals(trade.getBuyerCompleteFlag()) ) {
            trade.setTradeStatus(CANCEL);
        }
    }

}
