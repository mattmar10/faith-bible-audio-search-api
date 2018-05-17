package com.mattmartin.faithbible.audiosearchapi.models;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.tomcat.jni.Local;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;


@Document(indexName = "audio_data", type = "sermons")
public class SermonDocumentModel {

    @Id
    private String id;

    private String title;
    private String href;
    private String speaker;
    private LocalDate date;

    private String series;
    private Optional<String> seriesId;

    private Optional<String> image;
    private SermonMediaModel media;

    public SermonDocumentModel(){}

    public SermonDocumentModel(final String id, final String title, final String href, final String speaker,
                               final LocalDate date, final String series, Optional<String> seriesId,
                               final Optional<String> image, final SermonMediaModel media) {
        this.id = id;
        this.title = title;
        this.href = href;
        this.speaker = speaker;
        this.date = date;
        this.series = series;
        this.media = media;
        this.image = image;
        this.seriesId = seriesId;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
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

    public void setImage(Optional<String> imageUrl){ this.image = imageUrl; }

    public Optional<String> getImage(){ return this.image; };

    public Optional<String> getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(Optional<String> seriesId) {
        this.seriesId = seriesId;
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
                ", seriesId='" + seriesId + '\'' +
                ", media=" + media +
                '}';
    }
}
