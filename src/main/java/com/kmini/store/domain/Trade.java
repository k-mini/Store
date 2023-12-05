package com.kmini.store.domain;


import com.kmini.store.domain.type.CompleteFlag;
import com.kmini.store.domain.type.TradeStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Trade extends BaseTime{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRADE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID", foreignKey = @ForeignKey(name = "TradeToBoard"))
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BUYER_ID", foreignKey = @ForeignKey(name = "TradeToBuyer"))
    private User buyer;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "SELLER_ID", foreignKey = @ForeignKey(name = "TradeToSeller"))
//    private User seller;

    @Enumerated(EnumType.STRING)
    private TradeStatus tradeStatus;

    // 거래 완료 시 구매자가 버튼을 누를 경우 플래그 저장
    @Column(name="BUYER_COMPLETE")
    @Enumerated(EnumType.STRING)
    private CompleteFlag buyerCompleteFlag;
    // 거래 완료 시 판매자가 버튼을 누를 경우 플래그 저장
    @Column(name="SELLER_COMPLETE")
    @Enumerated(EnumType.STRING)
    private CompleteFlag sellerCompleteFlag;

    public User getSeller() {
        return this.getBoard().getUser();
    }

}
