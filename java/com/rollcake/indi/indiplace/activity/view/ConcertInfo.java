package com.rollcake.indi.indiplace.activity.view;

public class ConcertInfo {

    private String location;
    private String artistName;
    private String startTime;
    private String endTime;
    private String placeContent;
    private double lat;
    private double loc;

    public ConcertInfo(String artistName, String location, String startTime, String endTime, double lat, double loc , String placeContent) {
        this.location = location;
        this.artistName = artistName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.placeContent = placeContent;
        this.lat = lat;
        this.loc = loc;
    }

    public String getLocation() {
        return location;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public double getLat() {
        return lat;
    }

    public double getLoc() {
        return loc;
    }

    public String getPlaceContent() {
        return placeContent;
    }
}
