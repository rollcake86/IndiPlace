package com.rollcake.indi.indiplace.activity.view;

public class ArtistInfo {

    private String title;
    private String content;
    private String url;
    private String artistId;

    public ArtistInfo(String title , String content , String url , String artistId){
        this.title = title;
        this.content = content;
        this.url = url;
        this.artistId = artistId;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getArtistId() {
        return artistId;
    }
}
