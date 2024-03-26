package com.KociApp.RateGame.importGames;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TwitchTokenGetterTests {

    @Mock
    private TwitchService twitchService;

    @InjectMocks
    TwitchTokenGetter twitchTokenGetter;

    @Test
    public void TwitchTokenGetterSuccesTest() throws UnirestException {

        HttpResponse<JsonNode> mockResponse = Mockito.mock(HttpResponse.class); // Creating a mock HttpResponse
        JsonNode mockBody = Mockito.mock(JsonNode.class); // Creating a mock JsonNode for the response body

        String responseBody = "{\"access_token\": \"test_token123\"}";
        when(mockBody.getObject()).thenReturn(new JSONObject(responseBody));

        when(mockResponse.getStatus()).thenReturn(200);
        when(mockResponse.getBody()).thenReturn(mockBody);

        when(twitchService.getAccesToken()).thenReturn(mockResponse);

        String token = twitchTokenGetter.getAccesToken();

        Assertions.assertThat(token).isEqualTo("test_token123");
    }

    @Test
    public void TwitchTokenGetterFailureTest() throws UnirestException {

        HttpResponse<JsonNode> mockResponse = Mockito.mock(HttpResponse.class);
        when(mockResponse.getStatus()).thenReturn(404); // Status code other than 200

        // Mocking the response from TwitchService
        when(twitchService.getAccesToken()).thenReturn(mockResponse);

        // Creating the TwitchTokenGetter instance with the mocked TwitchService
        TwitchTokenGetter twitchTokenGetter = new TwitchTokenGetter(twitchService);

        // Assertions
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                twitchTokenGetter::getAccesToken,
                "Expected getAccessToken() to throw RuntimeException"
        );
        assertEquals("could not get twitch acces token, response code-404", exception.getMessage());
    }
}
