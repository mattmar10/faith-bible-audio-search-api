package com.mattmartin.faithbible.audiosearchapi.elasticsearch.models;


import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Document(indexName = "audio_data", type = "series")
public class SeriesModel {

    @Id
    private String id;
    private String title;
    private Optional<StatsModel> stats;
    private Optional<URI> imageURI;

    @Field(type = FieldType.Nested)
    private List<SermonDocumentModel> sermons;

   /*public static SeriesModel fromSermon(final SermonDocumentModel documentModel){
        final String title = documentModel.getSeries();
        final Optional<URI> imageURI = (documentModel.getImage().isPresent()) ?
                Optional.of(URI.create(documentModel.getImage().get())) :
                Optional.empty();

        return new SeriesModel(documentModel.getSeriesId().orElseGet(null), title, imageURI, Arrays.asList(documentModel));
    }*/

    public SeriesModel(){}

    public SeriesModel(final String id,
                       final String title,
                       final Optional<URI> imageURI){
        this.title = title;
        this.imageURI = imageURI;
        this.id = id;
        this.sermons = new ArrayList<>();
        this.stats = Optional.empty();
    }

    public SeriesModel(final String id,
                       final String title,
                       final List<SermonDocumentModel> sermons,
                       final Optional<URI> imageURI,
                       final Optional<StatsModel> stats){
        this.title = title;
        this.imageURI = imageURI;
        this.id = id;
        this.sermons = new ArrayList<SermonDocumentModel>(sermons);
        this.stats = stats;
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

    public Optional<URI> getImageURI() {
        return imageURI;
    }

    public void setImageURI(Optional<URI> imageURI) {
        this.imageURI = imageURI;
    }

    public List<SermonDocumentModel> getSermons() {
        return sermons;
    }

    public void setSermons(List<SermonDocumentModel> sermons) {
        this.sermons = sermons;
    }

    public boolean addSermon(final SermonDocumentModel sermonDocumentModel)
    {
        return this.sermons.add(sermonDocumentModel);
    }

    public Optional<StatsModel> getStats() {
        return stats;
    }

    public void setStats(Optional<StatsModel> stats) {
        this.stats = stats;
    }

    public boolean removeSermon(final SermonDocumentModel sermonDocumentModel){
       return this.sermons.remove(sermonDocumentModel);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SeriesModel series = (SeriesModel) o;

        return title.equals(series.title);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Series{");
        sb.append("id='").append(id).append('\'');
        sb.append("title='").append(title).append('\'');
        sb.append(", imageURI=").append(imageURI);
        sb.append(", status=").append(stats);
        sb.append('}');
        return sb.toString();
    }
}
