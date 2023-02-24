package org.example.controller;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.AsyncEvent;
import jakarta.servlet.AsyncListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import org.example.model.Audio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@WebServlet(urlPatterns = "/artists/*", asyncSupported = true)
public class ResourceServlet extends HttpServlet {

    private BlockingQueue<AsyncContext> acs = new LinkedBlockingQueue<>();
    private final Executor executor = Executors.newFixedThreadPool(10);

    private static final long serialVersionUID = 1L;
    ConcurrentHashMap<String, Object> audioDB = new ConcurrentHashMap<>();
    Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        audioDB.put("audio_1", new Audio("abc cents","Sample","Sample",14, 1990, 14, 20));
        audioDB.put("audio_2", new Audio("def","Sample","Sample",14, 1990, 14, 20));
        audioDB.put("audio_3", new Audio("ghi","Sample","Sample",14, 1990, 14, 20));
        audioDB.put("totalCopiesSoldNum", 60);

        new Thread(() -> {
            while (true) {
                try {
                    AsyncContext context = acs.take();

                    executor.execute(new MyService(context, audioDB));
                } catch (InterruptedException e) {
                    log(e.getMessage());
                }
            }
        }).start();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AsyncContext ac = request.startAsync();
        acs.add(ac);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AsyncContext ac = request.startAsync();
        acs.add(ac);
    }
}

class MyService implements Runnable {
    private AsyncContext ac;
    ConcurrentHashMap<String, Object> audioDB;
    public MyService(AsyncContext ac, ConcurrentHashMap<String, Object> audioDB) {
        this.ac = ac;
        this.audioDB = audioDB;
    }

    Gson gson = new Gson();
    @Override
    public void run() {
        // Check if request is a GET request
        HttpServletRequest request = (HttpServletRequest) ac.getRequest();
        HttpServletResponse response =  (HttpServletResponse) ac.getResponse();
        if(request.getMethod().equals("GET")){
            // Do something for GET requests
            try {
//	    		/coen6731/audios
                String[] pathParts = request.getRequestURI().split("/");
                if (pathParts[pathParts.length-1].equals("audios")) {
                    List<Object> audios = audioDB.entrySet().stream()
                            .filter(entry -> entry.getKey().matches("audio.*"))
                            .map(Map.Entry::getValue)
                            .collect(Collectors.toList());

                    JsonElement element = gson.toJsonTree(audios);
                    PrintWriter out = response.getWriter();
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");

                    out.println(element.toString());
                    out.flush();
                } else {
                    String audioIDString = pathParts[pathParts.length - 3];
                    String propertyName = pathParts[pathParts.length - 1];
                    Audio audio = (Audio) audioDB.get("audio_"+audioIDString);
                    String propertyValue = "";

                    if("artistName".equals(propertyName)) propertyValue = audio.getArtistName();
                    else if("trackTitle".equals(propertyName)) propertyValue = audio.getTrackTitle();
                    else if("albumTitle".equals(propertyName)) propertyValue = audio.getAlbumTitle();
                    else if("trackNumber".equals(propertyName)) propertyValue = Long.toString(audio.getTrackNumber());
                    else if("year".equals(propertyName)) propertyValue = Long.toString(audio.getYear());
                    else if("numReviews".equals(propertyName)) propertyValue = Long.toString(audio.getNumReviews());
                    else if("numCopiesSold".equals(propertyName)) propertyValue = Long.toString(audio.getNumCopiesSold());

                    PrintWriter out = response.getWriter();
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setCharacterEncoding("UTF-8");

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("propertyValue",propertyValue);
                    String jsonString = gson.toJson(jsonObject);

                    out.print(jsonString);
                    out.flush();
                }
                ac.complete();
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            // Post requests
            try {
                ObjectMapper mapper = new ObjectMapper();
                Audio audio = mapper.readValue(request.getInputStream(), Audio.class);
                // TODO Auto-generated catch block
                audioDB.put("audio_"+ audio.getArtistName(), audio);
                audioDB.compute("totalCopiesSoldNum", (key, value) -> (int)value + audio.getNumCopiesSold());
                response.setStatus(HttpServletResponse.SC_OK);
                response.getOutputStream().println("Audio name " + audio.getArtistName() + " is added to the database.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            ac.complete();
        }
    }
}


