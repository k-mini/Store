package com.kmini.store.domain;

import com.kmini.store.domain.type.TradeStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import static com.kmini.store.domain.type.TradeStatus.*;

@Entity
@DiscriminatorValue("I")
@NoArgsConstructor
@SuperBuilder
@Getter @Setter
public class ItemBoard extends Board{

    public ItemBoard(User user, String content, String itemName) {
        super(user, content);
        this.itemName = itemName;
    }
    private String itemName;

    @OneToMany(mappedBy = "tradeId")
    private List<Trade> trades = new ArrayList<>();

    public boolean hasCompletedTrade() {
        return hasTradeStatus(COMPLETE);
    }

    public boolean hasCanceledTrade() {
        return hasTradeStatus(CANCEL);
    }

    public boolean hasDealingTrade() {
        return hasTradeStatus(DEALING);
    }
    private boolean hasTradeStatus(TradeStatus complete) {
        return this.getTrades()
                .stream()
                .anyMatch(trade -> trade.getTradeStatus().equals(complete));
    }
}
