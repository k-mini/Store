package com.kmini.store.domain;

import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("I")
@NoArgsConstructor
public class ItemBoard extends Board{

    public ItemBoard(User user, BoardCategory category, String content, String itemName) {
        super(user, category, content);
        this.itemName = itemName;
    }
    private String itemName;
}
