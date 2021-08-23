package com.crud.tasks.trello.client;

import com.crud.tasks.trello.config.TrelloConfig;
import com.crud.tasks.trello.domain.*;
import com.crud.tasks.trello.mapper.TrelloMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrelloClientTest {

    @InjectMocks
    private TrelloClient trelloClient;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private TrelloConfig trelloConfig;

    @Mock
    private TrelloMapper trelloMapper;

    @Test
    public void shouldFetchTrelloBoards() throws URISyntaxException {
        //Given
        when(trelloConfig.getTrelloApiEndpoint()).thenReturn("http://test.com/");
        when(trelloConfig.getTrelloApiUsername()).thenReturn("test/");
        when(trelloConfig.getTrelloApiKey()).thenReturn("test");
        when(trelloConfig.getTrelloApiToken()).thenReturn("test");

        TrelloBoardDto[] trelloBoards = new TrelloBoardDto[1];
        trelloBoards[0] = new TrelloBoardDto("test_id", "test_board", new ArrayList<>());

        URI uri = new URI("http://test.com/members/test/boards?key=test&token=test&fields=name,id&lists=all");

        when(restTemplate.getForObject(uri, TrelloBoardDto[].class)).thenReturn(trelloBoards);

        //When
        List<TrelloBoardDto> fetchedTrelloBoards = trelloClient.getTrelloBoards();

        //Then
        assertEquals(1, fetchedTrelloBoards.size());
        assertEquals("test_id", fetchedTrelloBoards.get(0).getId());
        assertEquals("test_board", fetchedTrelloBoards.get(0).getName());
        assertEquals(new ArrayList<>(), fetchedTrelloBoards.get(0).getLists());
    }

    @Test
    public void shouldCreateCard() throws URISyntaxException {
        // Given
        when(trelloConfig.getTrelloApiEndpoint()).thenReturn("http://test.com");
        when(trelloConfig.getTrelloApiKey()).thenReturn("test");
        when(trelloConfig.getTrelloApiToken()).thenReturn("test");
        TrelloCardDto trelloCardDto = new TrelloCardDto(
                "Test task",
                "Test Description",
                "top",
                "test_id"
        );

        URI uri = new URI("http://test.com/cards?key=test&token=test&name=Test%20task&desc=Test%20Description&pos=top&idList=test_id");

        CreatedTrelloCardDto createdTrelloCardDto = new CreatedTrelloCardDto(
                "1",
                "test task",
                "http://test.com"
        );
        when(restTemplate.postForObject(uri, null, CreatedTrelloCardDto.class)).thenReturn(createdTrelloCardDto);

        // When
        CreatedTrelloCardDto newCard = trelloClient.createNewCard(trelloCardDto);

        // Then
        assertEquals("1", newCard.getId());
        assertEquals("test task", newCard.getName());
        assertEquals("http://test.com", newCard.getShortUrl());
    }

    @Test
    public void shouldReturnEmptyList() throws URISyntaxException {
        // Given
        when(trelloConfig.getTrelloApiEndpoint()).thenReturn("http://test.com/");
        when(trelloConfig.getTrelloApiUsername()).thenReturn("test/");
        when(trelloConfig.getTrelloApiKey()).thenReturn("test");
        when(trelloConfig.getTrelloApiToken()).thenReturn("test");
        URI uri = new URI("http://test.com/members/test/boards?key=test&token=test&fields=name,id&lists=all");

        when(restTemplate.getForObject(uri, TrelloBoardDto[].class)).thenReturn(null);

        // When
        List<TrelloBoardDto> trelloBoards = trelloClient.getTrelloBoards();

        //Then
        assertEquals(0, trelloBoards.size());
    }

    @Test
    public void shouldCorrectlyMapCardToDto() {
        //Given
        TrelloCard trelloCard = new TrelloCard("first card", "this is the first card", "top", "list_id");
        TrelloCardDto trelloCardDto = new TrelloCardDto("first card dto", "this is the first card dto", "top", "list_id");

        when(trelloMapper.mapToCardDto(trelloCard)).thenReturn(trelloCardDto);

        //When
        TrelloCardDto mappedTrelloCardDto = trelloMapper.mapToCardDto(trelloCard);

        //Then
        assertEquals("first card dto", mappedTrelloCardDto.getName());
        assertEquals("this is the first card dto", mappedTrelloCardDto.getDescription());
    }

    @Test
    public void shouldCorrectlyMapDtoToCard() {
        //Given
        TrelloCard trelloCard = new TrelloCard("second card", "this is the second card", "top", "list_id");
        TrelloCardDto trelloCardDto = new TrelloCardDto("second card dto", "this is the second card dto", "top", "list_id");

        when(trelloMapper.mapToCard(trelloCardDto)).thenReturn(trelloCard);

        //When
        TrelloCard mappedTrelloCard = trelloMapper.mapToCard(trelloCardDto);

        //Then
        assertEquals("second card", mappedTrelloCard.getName());
        assertEquals("this is the second card", mappedTrelloCard.getDescription());
    }


    @Test
    public void shouldCorrectlyMapListsToDto() {
        //Given
        TrelloList trelloList = new TrelloList("1", "first list", false);
        List<TrelloList> trelloLists = new ArrayList<>();
        trelloLists.add(trelloList);

        TrelloListDto trelloListDto = new TrelloListDto("1", "first list dto", false);
        List<TrelloListDto> trelloListsDto = new ArrayList<>();
        trelloListsDto.add(trelloListDto);

        when(trelloMapper.mapToListDto(trelloLists)).thenReturn(trelloListsDto);

        //When
        List<TrelloListDto> mappedTrelloListsDto = trelloMapper.mapToListDto(trelloLists);

        //Then
        assertEquals(1, mappedTrelloListsDto.size());
    }

    @Test
    public void shouldCorrectlyMapDtoToLists() {
        //Given
        TrelloList trelloList = new TrelloList("2", "second list", false);
        List<TrelloList> trelloLists = new ArrayList<>();
        trelloLists.add(trelloList);

        TrelloListDto trelloListDto = new TrelloListDto("2", "second list dto", false);
        List<TrelloListDto> trelloListsDto = new ArrayList<>();
        trelloListsDto.add(trelloListDto);

        when(trelloMapper.mapToList(trelloListsDto)).thenReturn(trelloLists);

        //When
        List<TrelloList> mappedTrelloLists = trelloMapper.mapToList(trelloListsDto);

        //Then
        assertEquals(1, mappedTrelloLists.size());
    }

    @Test
    public void shouldCorrectlyMapBoardsToDto() {
        //Given
        TrelloList trelloList = new TrelloList("1", "first list", false);
        List<TrelloList> trelloLists = new ArrayList<>();
        trelloLists.add(trelloList);
        TrelloBoard trelloBoard = new TrelloBoard("1", "first board", trelloLists);
        List<TrelloBoard> trelloBoards = new ArrayList<>();
        trelloBoards.add(trelloBoard);

        TrelloListDto trelloListDto = new TrelloListDto("1", "first list dto", false);
        List<TrelloListDto> trelloListsDto = new ArrayList<>();
        trelloListsDto.add(trelloListDto);
        TrelloBoardDto trelloBoardDto = new TrelloBoardDto("1", "first board dto", trelloListsDto);
        List<TrelloBoardDto> trelloBoardsDto = new ArrayList<>();
        trelloBoardsDto.add(trelloBoardDto);


        when(trelloMapper.mapToBoardsDto(trelloBoards)).thenReturn(trelloBoardsDto);

        //When
        List<TrelloBoardDto> mappedTrelloBoardsDto = trelloMapper.mapToBoardsDto(trelloBoards);

        //Then
        assertEquals(1, mappedTrelloBoardsDto.size());
    }

    @Test
    public void shouldCorrectlyMapToBoardsDto() {
        //Given
        TrelloList trelloList = new TrelloList("2", "second list", false);
        List<TrelloList> trelloLists = new ArrayList<>();
        trelloLists.add(trelloList);
        TrelloBoard trelloBoard = new TrelloBoard("2", "second board", trelloLists);
        List<TrelloBoard> trelloBoards = new ArrayList<>();
        trelloBoards.add(trelloBoard);

        TrelloListDto trelloListDto = new TrelloListDto("2", "second list dto", false);
        List<TrelloListDto> trelloListsDto = new ArrayList<>();
        trelloListsDto.add(trelloListDto);
        TrelloBoardDto trelloBoardDto = new TrelloBoardDto("1", "second board dto", trelloListsDto);
        List<TrelloBoardDto> trelloBoardsDto = new ArrayList<>();
        trelloBoardsDto.add(trelloBoardDto);


        when(trelloMapper.mapToBoards(trelloBoardsDto)).thenReturn(trelloBoards);

        //When
        List<TrelloBoard> mappedTrelloBoards = trelloMapper.mapToBoards(trelloBoardsDto);

        //Then
        assertEquals(1, mappedTrelloBoards.size());
    }
}