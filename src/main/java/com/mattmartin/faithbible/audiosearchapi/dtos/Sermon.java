package com.mattmartin.faithbible.audiosearchapi.dtos;

public class Sermon {

    private String id;
    private String title;
    private String speaker;
    private String series;


    public Sermon(final String id, final String name, final String speaker, final String series){
        this.id = id;
        this.title = name;
        this.speaker = speaker;
        this.series = series;
    }


    public String getId(){
        return this.id;
    }


    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }
}
