package com.kmini.store.controller.api;


import com.kmini.store.service.ItemBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardApiController {

    private final ItemBoardService itemBoardService;


}
