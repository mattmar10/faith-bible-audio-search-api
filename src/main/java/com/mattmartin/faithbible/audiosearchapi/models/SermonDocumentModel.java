package com.mattmartin.faithbible.audiosearchapi.models;


import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;


@Document(indexName = "audio_data", type = "sermons")
public class SermonDocumentModel {

    @Id
    private String id;

    private String title;
    private String href;
    private String speaker;
    private String date;
    private String series;

    private SermonMediaModel media;

    public SermonDocumentModel(){}

    public SermonDocumentModel(final String id, final String title, final String href, final String speaker,
                               final String date, final String series, final SermonMediaModel media) {
        this.id = id;
        this.title = title;
        this.href = href;
        this.speaker = speaker;
        this.date = date;
        this.series = series;
        this.media = media;
    }

    public String getId() {
        return id;
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

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public SermonMediaModel getMedia() {
        return media;
    }

    public void setMedia(SermonMediaModel media) {
        this.media = media;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SermonDocumentModel that = (SermonDocumentModel) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "SermonDocumentModel{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", href='" + href + '\'' +
                ", speaker='" + speaker + '\'' +
                ", date=" + date +
                ", series='" + series + '\'' +
                ", media=" + media +
                '}';
    }
}
