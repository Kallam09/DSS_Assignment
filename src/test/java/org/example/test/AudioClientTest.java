package org.example.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.example.model.Audio;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


public class AudioClientTest {
    private static final String GET_ALL_ARTISTS_URL = "http://155.248.227.33:8080/audio0923/artists";

    private static final int[] NUM_CLIENTS = {10, 50, 100};
    private static final int[] CLIENT_RATIOS = {2,5,10,20};
    private static final String[] ARTISTS = {"artist1", "artist2", "artist3"};

    public static void main(String[] args) throws Exception {
        for (int numClients : NUM_CLIENTS) {
            for (int clientRatio : CLIENT_RATIOS) {
                int numGetPropertyRequests = numClients * clientRatio / (clientRatio + clientRatio);
                int numGetAllArtistsRequests = numClients - numGetPropertyRequests;
                int numPostRequests = numClients - numGetPropertyRequests - numGetAllArtistsRequests;
                ExecutorService executor = Executors.newFixedThreadPool(numClients);
                org.eclipse.jetty.client.HttpClient client = new HttpClient();
                client.start();

                List<Long> roundTimes = new ArrayList<>();
                long totalStartTime = System.currentTimeMillis();
                for (int i = 0; i < numClients; i++) {
                    int clientID = i + 1;
                    executor.execute(() -> {
                        // get request
                        for (int j = 0; j < clientRatio; j++) {
                            try {
                                // calculate round-time
                                long startTime = System.currentTimeMillis();

                                ContentResponse res = client.GET(GET_ALL_ARTISTS_URL);
                                assertThat(res.getStatus(), equalTo(200));

                                long roundTime = System.currentTimeMillis() - startTime;
                                roundTimes.add(roundTime);

                            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                                System.out.print(e.getMessage());
                            }
                        }

                        // post request
                        ObjectMapper mapper = new ObjectMapper();
                        Audio audio = new Audio("3" + clientID, "sample", "sample", 14, 1990, 14, 20);
                        try {
                            String jsonString = mapper.writeValueAsString(audio);

                            // calculate round-time
                            long startTime = System.currentTimeMillis();

                            ContentResponse res = client.POST(GET_ALL_ARTISTS_URL)
                                    .content(new StringContentProvider(jsonString), "application/json")
                                    .send();
                            assertThat(res.getStatus(), equalTo(200));
//					System.out.println(res.getContentAsString());

                            long roundTime = System.currentTimeMillis() - startTime;
                            roundTimes.add(roundTime);

                        } catch (InterruptedException | TimeoutException | ExecutionException |
                                 JsonProcessingException e) {
                            System.out.print(e.getMessage());
                        }
                    });
                }
                executor.shutdown();
                executor.awaitTermination(10, TimeUnit.MINUTES);
                long totalRoundTime = System.currentTimeMillis() - totalStartTime;
                System.out.println("Round-trip time list(ms): " + roundTimes);
                System.out.println("The number of clients: " + numClients + ", ratio: " + clientRatio + ":1, total round-trip time: " + totalRoundTime + "ms");


            }
        }
    }
}

