package com.kmini.store.domain.entity;

import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("C")
@AllArgsConstructor @SuperBuilder
public class CommunityBoard extends Board {

}
