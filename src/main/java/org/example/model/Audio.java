package org.example.model;

public class Audio {
    private String artistName;
    private String trackTitle;
    private String albumTitle;
    private int trackNumber;
    private int year;
    private int numReviews;
    private int numCopiesSold;

    // Constructor
    public Audio(String artistName, String trackTitle, String albumTitle, int trackNumber, int year, int numReviews, int numCopiesSold) {
        this.artistName = artistName;
        this.trackTitle = trackTitle;
        this.albumTitle = albumTitle;
        this.trackNumber = trackNumber;
        this.year = year;
        this.numReviews = numReviews;
        this.numCopiesSold = numCopiesSold;
    }

    // GET methods
    public String getArtistName() {
        return artistName;
    }

    public String getTrackTitle() {
        return trackTitle;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public int getYear() {
        return year;
    }

    public int getNumReviews() {
        return numReviews;
    }

    public int getNumCopiesSold() {
        return numCopiesSold;
    }

    // POST method
    public void createAudioItem(String artistName, String trackTitle, String albumTitle, int trackNumber, int year, int numReviews, int numCopiesSold) {
        this.artistName = artistName;
        this.trackTitle = trackTitle;
        this.albumTitle = albumTitle;
        this.trackNumber = trackNumber;
        this.year = year;
        this.numReviews = numReviews;
        this.numCopiesSold = numCopiesSold;
    }

    public void setArtistName(String artistName) {
    }

    public void setTrackTitle(String trackTitle) {
    }

    public void setAlbumTitle(String albumTitle) {
    }

    public void setTrackNumber(int trackNumber) {
    }

    public void setYear(int year) {
    }

    public void setNumReviews(int numReviews) {
    }

    public void setNumCopiesSold(int numCopiesSold) {
    }

    @Override
    public String toString() {
        return "Audio [artistName=" + artistName + ", trackTitle=" + trackTitle + ", albumTitle="
                + albumTitle + ", trackNumber=" + trackNumber + ", year=" + year + ", numReviews=" + numReviews
                + ", numCopiesSold=" + numCopiesSold + "]";
    }
}
