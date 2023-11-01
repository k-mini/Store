package com.kmini.store.domain;

import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("C")
@NoArgsConstructor
public class CommunityBoard extends Board {

    public CommunityBoard(User user, BoardCategory category, String content) {
        super(user, category, content);
    }
}
