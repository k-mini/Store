package com.kmini.store.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

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
}
