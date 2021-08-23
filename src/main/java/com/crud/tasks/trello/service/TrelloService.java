package com.crud.tasks.trello.service;

import com.crud.tasks.config.AdminConfig;
import com.crud.tasks.domain.Mail;
import com.crud.tasks.service.SimpleEmailService;
import com.crud.tasks.trello.client.TrelloClient;
import com.crud.tasks.trello.domain.CreatedTrelloCardDto;
import com.crud.tasks.trello.domain.TrelloBoardDto;
import com.crud.tasks.trello.domain.TrelloCardDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class TrelloService {
    private static final String SUBJECT = "Tasks: New Trello card";

    private final TrelloClient trelloClient;
    private final SimpleEmailService emailService;
    private final AdminConfig adminConfig;

    public List<TrelloBoardDto> fetchTrelloBoards() {
        return trelloClient.getTrelloBoards();
    }

    public CreatedTrelloCardDto createTrelloCard(final TrelloCardDto trelloCardDto) {
        CreatedTrelloCardDto newCard = trelloClient.createNewCard(trelloCardDto);

        ofNullable(newCard).ifPresent(card ->
                emailService.send(
                        Mail.builder()
                                .mailTo(adminConfig.getAdminMail())
                                .subject(SUBJECT)
                                .message("New card: " + card.getName() + " has been created on your Trello board")
                                .toCc(adminConfig.getAdminMail())
                                .build()
                ));
        return newCard;
    }
}
