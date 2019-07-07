package de.flexusma.wavybot.utils;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gson.Gson;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collection;

public class YoutubeSearch {

    public static SearchListResponse search(String title) {
        title = title.replaceAll(" ", "+");
        try {
            SearchListResponse response = getResp(title);

            return response;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private static synchronized YouTube getTubeService() {

        YouTube tubeService = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(),
                new HttpRequestInitializer() {
                    @Override
                    public void initialize(HttpRequest request) throws IOException {
                        // Nothing?
                    }
                }).setApplicationName("Wavvy").build();

        return tubeService;
    }


    private static SearchListResponse getResp(String q)
            throws GeneralSecurityException, IOException, GoogleJsonResponseException {
        YouTube youtubeService = getTubeService();
        // Define and execute the API request
        YouTube.Search.List request = youtubeService.search()
                .list("snippet");
        SearchListResponse response = request.setMaxResults(25L)
                .setQ(q)
                .setKey(BotToken.ytapikey)
                .execute();
        System.out.println(response);
        return response;
    }
}



