package com.kmini.store.domain;


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

    public User getSeller() {
        return this.getBoard().getUser();
    }

    public void checkBuyerOrSellerIsMe() {
        Long buyerId = this.getBuyer().getId();
        Long sellerId = this.getSeller().getId();

        User user = User.getSecurityContextUser();

        // 상관 없는 사용자 거부
        if (!user.getId().equals(buyerId) && !user.getId().equals(sellerId)) {
            throw new IllegalArgumentException("판매자나 구매자가 아닙니다.");
        }
    }
}
