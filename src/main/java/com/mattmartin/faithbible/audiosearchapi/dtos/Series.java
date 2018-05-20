package com.mattmartin.faithbible.audiosearchapi.dtos;

import com.mattmartin.faithbible.audiosearchapi.models.SeriesModel;
import com.mattmartin.faithbible.audiosearchapi.models.SermonDocumentModel;
import org.springframework.util.CollectionUtils;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Series {

    private String id;
    private String title;
    private Optional<URI> imageURI;
    private Optional<List<Sermon>> sermons;

    public Series(final SeriesModel seriesModel){
        this.title = seriesModel.getTitle();
        this.imageURI = seriesModel.getImageURI();
        this.id = seriesModel.getId();

        final List<SermonDocumentModel> sermonModels = seriesModel.getSermons();
        this.sermons = (CollectionUtils.isEmpty(sermonModels)) ?
                Optional.empty() :
                Optional.of(
                        sermonModels.stream()
                                .map(s -> Sermon.fromModel(s))
                                .collect(Collectors.toList()));
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Optional<List<Sermon>> getSermons() {
        return sermons;
    }

    public void setSermons(Optional<List<Sermon>> sermons) {
        this.sermons = sermons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Series series = (Series) o;

        if (!id.equals(series.id)) return false;
        if (!title.equals(series.title)) return false;
        return imageURI != null ? imageURI.equals(series.imageURI) : series.imageURI == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + title.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Series{");
        sb.append("id='").append(id).append('\'');
        sb.append("title='").append(title).append('\'');
        sb.append(", imageURI=").append(imageURI);
        sb.append('}');
        return sb.toString();
    }
}
