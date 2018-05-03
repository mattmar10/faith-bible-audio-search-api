package com.mattmartin.faithbible.audiosearchapi.dtos;

import java.time.LocalDate;

public class Sermon {

    private String id;
    private String title;
    private String speaker;
    private String series;
    private LocalDate date;

    public Sermon(final String id,
                  final String name,
                  final String speaker,
                  final String series,
                  final LocalDate date){
        this.id = id;
        this.title = name;
        this.speaker = speaker;
        this.series = series;
        this.date = date;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
