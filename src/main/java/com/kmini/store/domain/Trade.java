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

}
