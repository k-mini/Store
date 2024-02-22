package com.kmini.store.config.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PageAttr {

    private int startPage;
    private int endPage;
    private boolean prev;
    private boolean next;
}
