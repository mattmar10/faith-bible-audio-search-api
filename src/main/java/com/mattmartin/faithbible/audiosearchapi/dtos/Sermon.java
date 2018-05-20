package com.mattmartin.faithbible.audiosearchapi.dtos;

import com.mattmartin.faithbible.audiosearchapi.models.SermonDocumentModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;

public class Sermon {
    private static final Logger logger = LoggerFactory.getLogger(Sermon.class);

    private String id;
    private String title;
    private String speaker;
    private String series;

    private Optional<String> seriesLink;
    private LocalDate date;
    private Optional<URI> mp3URI;
    private Optional<URI> imageURI;
    private Optional<URI> pdfURI;

    public static Sermon fromModel(final SermonDocumentModel documentModel) throws IllegalStateException{
        final String docId = documentModel.getId();
        final String title = documentModel.getTitle().contains("(") ?
                documentModel.getTitle().substring(0, documentModel.getTitle().indexOf("(") - 1) :
                documentModel.getTitle();
        final String speaker = documentModel.getSpeaker();
        final String series = documentModel.getSeries();
        final LocalDate date = documentModel.getDate();
        final Optional<URI> mp3URI = documentModel.getMedia().getMp3().map(URI::create);
        final Optional<URI> imageURI = documentModel.getImage().map(URI::create);
        final Optional<URI> pdfURI = documentModel.getMedia().getPdf().map(URI::create);
        final Optional<String> seriesLink = documentModel.getSeriesId().map(id -> "/series/" + id);

        return new Sermon(docId, title, speaker, series, seriesLink, date, mp3URI, imageURI, pdfURI );

    }

    private Sermon( final String id,
                   final String title,
                   final String speaker,
                   final String series,
                   final Optional<String> seriesLink,
                   final LocalDate date,
                   final Optional<URI> mp3URI,
                   final Optional<URI> imageURI,
                   final Optional<URI> pdfURI ) {

        this.id = id;
        this.title = title;
        this.speaker = speaker;
        this.series = series;
        this.seriesLink = seriesLink;
        this.date = date;
        this.mp3URI = mp3URI;
        this.imageURI = imageURI;
        this.pdfURI = pdfURI;
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

    public Optional<URI> getImageURI() {
        return imageURI;
    }

    public void setImageURI(Optional<URI> imageURI) {
        this.imageURI = imageURI;
    }

    public Optional<URI> getMp3URI() {
        return mp3URI;
    }

    public void setMp3URI(Optional<URI> mp3URI) {
        this.mp3URI = mp3URI;
    }

    public Optional<URI> getPdfURI() {
        return pdfURI;
    }

    public void setPdfURI(Optional<URI> pdfURI) {
        this.pdfURI = pdfURI;
    }

    public Optional<String> getSeriesLink() {
        return seriesLink;
    }

    public void setSeriesLink(Optional<String> seriesLink) {
        this.seriesLink = seriesLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sermon sermon = (Sermon) o;

        if (!id.equals(sermon.id)) return false;
        if (!title.equals(sermon.title)) return false;
        if (!speaker.equals(sermon.speaker)) return false;
        if (!series.equals(sermon.series)) return false;
        if (!date.equals(sermon.date)) return false;
        if (!mp3URI.equals(sermon.mp3URI)) return false;
        if (!imageURI.equals(sermon.imageURI)) return false;
        return pdfURI.equals(sermon.pdfURI);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + speaker.hashCode();
        result = 31 * result + series.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + mp3URI.hashCode();
        result = 31 * result + pdfURI.hashCode();
        result = 31 * result + imageURI.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Sermon{");
        sb.append("id='").append(id).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append(", speaker='").append(speaker).append('\'');
        sb.append(", series='").append(series).append('\'');
        sb.append(", date=").append(date);
        sb.append(", mp3URI=").append(mp3URI);
        sb.append(", pdfURI=").append(pdfURI);
        sb.append(", imageURI=").append(imageURI);
        sb.append('}');
        return sb.toString();
    }
}
