package com.mattmartin.faithbible.audiosearchapi.elasticsearch.models;


import com.mattmartin.faithbible.audiosearchapi.dtos.Stats;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;


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
    private Optional<StatsModel> stats;
    private Optional<Set<String>> tags;

    public SermonDocumentModel(){}

    public SermonDocumentModel(final String id,
                               final String title,
                               final String href,
                               final String speaker,
                               final LocalDate date,
                               final String series,
                               final SermonMediaModel media,
                               final Optional<String> seriesId,
                               final Optional<StatsModel> stats,
                               final Optional<String> image,
                               final Optional<Set<String>> tags) {
        this.id = id;
        this.title = title;
        this.href = href;
        this.speaker = speaker;
        this.date = date;
        this.series = series;
        this.media = media;
        this.image = image;
        this.seriesId = seriesId;
        this.stats = stats;
        this.tags = tags;
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

    public Optional<StatsModel> getStats() {
        return stats;
    }

    public void setStats(Optional<StatsModel> stats) {
        this.stats = stats;
    }
    public Optional<Set<String>> getTags() {
        return tags;
    }

    public void setTags(Optional<Set<String>> tags) {
        this.tags = tags;
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
                ", stats='" + stats + '\'' +
                ", tags='" + tags + '\'' +
                '}';
    }
}
