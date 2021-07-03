package com.crud.tasks.controller;

import com.crud.tasks.trello.client.TrelloClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/trello")
@RequiredArgsConstructor
public class TrelloController {

    private final TrelloClient trelloClient;

    @GetMapping("getTrelloBoards")
    public void getTrelloBoards() {

        trelloClient.getTrelloBoards().stream()
                .filter(trelloBoardDto -> trelloBoardDto.getName().contains("Kodilla"))
                .collect(Collectors.toList())
                .forEach(trelloBoard -> System.out.println(trelloBoard.getName()));
    }
}
