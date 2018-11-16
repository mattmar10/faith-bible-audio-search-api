package com.mattmartin.faithbible.audiosearchapi.dtos;

import com.mattmartin.faithbible.audiosearchapi.db.models.SermonDBModel;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.models.SermonDocumentModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Sermon {
    private static final Logger logger = LoggerFactory.getLogger(Sermon.class);

    private Integer id;
    private String title;
    private String slug;
    private String speaker;
    private String series;
    private LocalDate date;

    private Optional<String> seriesLink;
    private Optional<String> seriesSlug;
    private Optional<URI> mp3URI;
    private Optional<URI> imageURI;
    private Optional<URI> pdfURI;
    private Optional<Stats> stats;
    private Optional<Set<String>> tags;

    public static Sermon fromModel(final SermonDocumentModel documentModel) throws IllegalStateException{
        final Integer docId = documentModel.getId();
        final String title = documentModel.getTitle().contains("(") ?
                documentModel.getTitle().substring(0, documentModel.getTitle().indexOf("(") - 1) :
                documentModel.getTitle();
        final String slug = documentModel.getSlug();
        final String speaker = documentModel.getSpeaker();
        final String series = documentModel.getSeries();
        final LocalDate date = documentModel.getDate();
        final Optional<URI> mp3URI = documentModel.getMedia().getMp3().map(URI::create);
        final Optional<URI> imageURI = documentModel.getImage().map(URI::create);
        final Optional<URI> pdfURI = documentModel.getMedia().getPdf().map(URI::create);
        final Optional<String> seriesLink = documentModel.getSeriesId().map(id -> "/series/" + id);
        final Optional<String> seriesSlug = documentModel.getSeriesSlug();

        final Optional<Stats> stats =
                documentModel.getStats().map(
                        statsModel -> new Stats(statsModel.getPlays(), statsModel.getLikes(), statsModel.getShares()));

        final Optional<Set<String>> tags = documentModel.getTags();

        return new Sermon(docId, title, slug, speaker, series, seriesLink, seriesSlug, date, mp3URI, imageURI, pdfURI, stats, tags);

    }

    public static Sermon fromDBModel(final SermonDBModel sermonDBModel){
        final int id = sermonDBModel.getId();
        final String title = sermonDBModel.getTitle();
        final String slug = sermonDBModel.getSlug();
        final String speaker = sermonDBModel.getSpeaker();
        final String series = sermonDBModel.getSeries().getTitle();
        final String seriesSlug = sermonDBModel.getSeries().getSlug();
        final LocalDate date = sermonDBModel.getDate();
        final String seriesLink = "/series/" + sermonDBModel.getSeries().getId();

        final Optional<URI> mp3URI = Optional.ofNullable(sermonDBModel.getMp3Url()).map(URI::create);
        final Optional<URI> imageURI = Optional.ofNullable(sermonDBModel.getImageUrl()).map(URI::create);
        final Optional<URI> pdfURI = Optional.ofNullable(sermonDBModel.getPdfUrl()).map(URI::create);


        final Stats stats = new Stats(sermonDBModel.getPlays(), sermonDBModel.getLikes(), sermonDBModel.getShares());

        final Optional<List<String>> tags = Optional.ofNullable(sermonDBModel.getTags());

        final Optional<Set<String>> tagSet = tags.map(list -> new HashSet(list));

        return new Sermon(id, title, slug, speaker, series, Optional.of(seriesLink), Optional.of(seriesSlug), date, mp3URI, imageURI, pdfURI, Optional.of(stats), tagSet);
    }

    public Sermon(){}

    private Sermon( final Integer id,
                   final String title,
                   final String slug,
                   final String speaker,
                   final String series,
                   final Optional<String> seriesLink,
                   final Optional<String> seriesSlug,
                   final LocalDate date,
                   final Optional<URI> mp3URI,
                   final Optional<URI> imageURI,
                   final Optional<URI> pdfURI,
                   final Optional<Stats> stats,
                    final Optional<Set<String>> tags) {

        this.id = id;
        this.title = title;
        this.slug = slug;
        this.speaker = speaker;
        this.series = series;
        this.seriesLink = seriesLink;
        this.seriesSlug = seriesSlug;
        this.date = date;
        this.mp3URI = mp3URI;
        this.imageURI = imageURI;
        this.pdfURI = pdfURI;
        this.stats = stats;
        this.tags = tags;
    }

    public Integer getId(){
        return this.id;
    }

    public void setId(Integer id) {
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

    public Optional<Stats> getStats() {
        return stats;
    }

    public void setStats(Optional<Stats> stats) {
        this.stats = stats;
    }

    public Optional<Set<String>> getTags() {
        return tags;
    }

    public void setTags(Optional<Set<String>> tags) {
        this.tags = tags;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Optional<String> getSeriesSlug() {
        return seriesSlug;
    }

    public void setSeriesSlug(Optional<String> seriesSlug) {
        this.seriesSlug = seriesSlug;
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

        if(date != null){
            result = 31 * result + date.hashCode();
        }

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
        sb.append(", seriesLink='").append(seriesLink).append('\'');
        sb.append(", seriesSlug='").append(seriesSlug).append('\'');
        sb.append(", slug='").append(slug).append('\'');
        sb.append(", date=").append(date);
        sb.append(", mp3URI=").append(mp3URI);
        sb.append(", pdfURI=").append(pdfURI);
        sb.append(", imageURI=").append(imageURI);
        sb.append(", stats=").append(stats);
        sb.append(", tags=").append(tags);
        sb.append('}');
        return sb.toString();
    }
}
