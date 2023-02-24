package org.example.controller;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet(name = "AudioServlet", value = "AudioServlet")
public class AudioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Audio item properties
    private String artistName;
    private String trackTitle;
    private String albumTitle;
    private int trackNumber;
    private int year;
    private int numReviews;
    private int numCopiesSold;

    // Constructor
    public AudioServlet() {
        super();
    }

    // GET method
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String propertyName = request.getParameter("property");
        if (propertyName == null) {
            out.println("Error: Property name is missing");
            return;
        }

        switch (propertyName) {
            case "artistName":
                out.println("Artist Name: " + artistName);
                break;
            case "trackTitle":
                out.println("Track Title: " + trackTitle);
                break;
            case "albumTitle":
                out.println("Album Title: " + albumTitle);
                break;
            case "trackNumber":
                out.println("Track Number: " + trackNumber);
                break;
            case "year":
                out.println("Year: " + year);
                break;
            case "numReviews":
                out.println("Number of Reviews: " + numReviews);
                break;
            case "numCopiesSold":
                out.println("Number of Copies Sold: " + numCopiesSold);
                break;
            default:
                out.println("Error: Property name is invalid");
                break;
        }
    }

    // POST method
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        artistName = request.getParameter("artistName");
        trackTitle = request.getParameter("trackTitle");
        albumTitle = request.getParameter("albumTitle");
        trackNumber = Integer.parseInt(request.getParameter("trackNumber"));
        year = Integer.parseInt(request.getParameter("year"));
        numReviews = Integer.parseInt(request.getParameter("numReviews"));
        numCopiesSold = Integer.parseInt(request.getParameter("numCopiesSold"));

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("Audio item has been created");
    }
}
