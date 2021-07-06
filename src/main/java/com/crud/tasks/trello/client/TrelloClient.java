package com.crud.tasks.trello.client;

import com.crud.tasks.domain.CreatedTrelloCard;
import com.crud.tasks.domain.TrelloBoardDto;
import com.crud.tasks.domain.TrelloCardDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TrelloClient {

    private final RestTemplate restTemplate;

    @Value("${trello.api.endpoint.prod}")
    private String trelloApiEndpoint;
    @Value("${trello.api.key}")
    private String trelloApiKey;
    @Value("${trello.api.token}")
    private String trelloApiToken;
    @Value("${trello.api.username}")
    private String trelloApiUsername;

    private URI getTrelloUrl() {

        return UriComponentsBuilder.fromHttpUrl(
                trelloApiEndpoint + "members/" + trelloApiUsername + "boards"
        )
                .queryParam("key", trelloApiKey)
                .queryParam("token", trelloApiToken)
                .queryParam("fields", "name,id")
                .queryParam("lists", "all")
                .build().encode().toUri();
    }

    public List<TrelloBoardDto> getTrelloBoards() {

        TrelloBoardDto[] boardResponse = restTemplate.getForObject(getTrelloUrl(), TrelloBoardDto[].class);

        return Optional.ofNullable(boardResponse)
                .map(Arrays::asList)
                .orElse(Collections.emptyList());
    }

    public CreatedTrelloCard createNewCard(TrelloCardDto trelloCardDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(trelloApiEndpoint + "/cards")
                .queryParam("key", trelloApiKey)
                .queryParam("token", trelloApiToken)
                .queryParam("name", trelloCardDto.getName())
                .queryParam("desc", trelloCardDto.getDescription())
                .queryParam("pos", trelloCardDto.getPos())
                .queryParam("idList", trelloCardDto.getListId())
                .queryParam("badges", trelloCardDto.getBadges())
                .build()
                .encode()
                .toUri();

        return restTemplate.postForObject(url, null, CreatedTrelloCard.class);
    }
}
