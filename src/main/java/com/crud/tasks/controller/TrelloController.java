package com.crud.tasks.controller;

import com.crud.tasks.domain.TrelloBoardDto;
import com.crud.tasks.trello.client.TrelloClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/trello")
@RequiredArgsConstructor
public class TrelloController {

    private final TrelloClient trelloClient;

    @GetMapping("getTrelloBoards")
    public void getTrelloBoards() {

        List<TrelloBoardDto> trelloBoards = trelloClient.getTrelloBoards();

        List<TrelloBoardDto> filteredTrelloBoards = trelloBoards.stream()
                .filter(trelloBoardDto -> Optional.ofNullable(trelloBoardDto).isPresent())
                .filter(trelloBoardDto -> trelloBoardDto.getName().contains("Kodilla")
        ).collect(Collectors.toList());

        filteredTrelloBoards.forEach(trelloBoard -> System.out.println(trelloBoard.getName() ));

    }
}
