package com.mattmartin.faithbible.audiosearchapi.dtos;

import com.mattmartin.faithbible.audiosearchapi.db.models.SeriesDBModel;
import com.mattmartin.faithbible.audiosearchapi.db.models.SermonDBModel;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.models.SeriesModel;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.models.SermonDocumentModel;
import org.springframework.util.CollectionUtils;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Series {

    private Integer id;
    private String title;
    private String slug;
    private Optional<URI> imageURI;
    private Optional<List<Sermon>> sermons;
    private Optional<Stats> stats;
    private Optional<Set<String>> tags;

    public Series(final SeriesModel seriesModel){
        this.title = seriesModel.getTitle();
        this.slug = seriesModel.getSlug();
        this.imageURI = seriesModel.getImageURI();
        this.id = seriesModel.getId();

        final List<SermonDocumentModel> sermonModels = seriesModel.getSermons();
        this.sermons = (CollectionUtils.isEmpty(sermonModels)) ?
                Optional.empty() :
                Optional.of(
                        sermonModels.stream()
                                .map(s -> Sermon.fromModel(s))
                                .collect(Collectors.toList()));

        this.stats = seriesModel.getStats().map(
                statsModel -> new Stats(statsModel.getPlays(), statsModel.getLikes(), statsModel.getShares()));

        tags = Optional.empty();
    }

    public Series(final SeriesDBModel seriesDBModel){

        this.id = seriesDBModel.getId();
        this.slug = seriesDBModel.getSlug();
        this.title = seriesDBModel.getTitle();
        this.imageURI = Optional.of(URI.create(seriesDBModel.getImageURL()));

        final List<SermonDBModel> sermonDBModels = seriesDBModel.getSermons();
        this.sermons = (CollectionUtils.isEmpty(sermonDBModels)) ?
                Optional.empty() :
                Optional.of(
                        sermonDBModels.stream()
                            .map(s -> Sermon.fromDBModel(s))
                        .collect(Collectors.toList())
                );

        final Stats stats = new Stats(seriesDBModel.getPlays(), seriesDBModel.getLikes(), seriesDBModel.getShares());
        this.stats = Optional.of(stats);

        this.tags = Optional.ofNullable(seriesDBModel.getTags()).map(tagList -> new HashSet<>(tagList));

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Optional<URI> getImageURI() {
        return imageURI;
    }

    public void setImageURI(Optional<URI> imageURI) {
        this.imageURI = imageURI;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Optional<List<Sermon>> getSermons() {
        return sermons;
    }

    public void setSermons(Optional<List<Sermon>> sermons) {
        this.sermons = sermons;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Series series = (Series) o;

        if (!id.equals(series.id)) return false;
        if (!title.equals(series.title)) return false;
        if (!slug.equals(series.slug)) return false;
        return imageURI != null ? imageURI.equals(series.imageURI) : series.imageURI == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + slug.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Series{");
        sb.append("id='").append(id).append('\'');
        sb.append("title='").append(title).append('\'');
        sb.append("slug='").append(slug).append('\'');
        sb.append(", imageURI=").append(imageURI);
        sb.append(", stats=").append(stats);
        sb.append(", tags=").append(tags);
        sb.append('}');
        return sb.toString();
    }
}
