package com.mattmartin.faithbible.audiosearchapi.dtos;

import com.mattmartin.faithbible.audiosearchapi.elasticsearch.models.SeriesModel;
import org.junit.Test;

import java.net.URI;
import java.util.HashSet;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SeriesTest
{
    @Test
    public void testSeries(){
        final HashSet<String> set = new HashSet<>();
        set.add("tag1");
        set.add("tag2");

        final URI imageURI = URI.create("http://edmondfaithbible.com/?page_id=2743&show&file_name=2015_0621%20Fathers%20Day%20Exodus%2020_12.mp3");
        final SeriesModel seriesModel = new SeriesModel(5, "SomeTitle", "slug", Optional.of(imageURI), Optional.of(set));
        final Series series = new Series(seriesModel);

        assertThat(series.getId(),  is(seriesModel.getId()));
        assertThat(series.getTitle(),  is(seriesModel.getTitle()));
        assertThat(series.getImageURI(),  is(seriesModel.getImageURI()));
    }

    @Test
    public void testSeriesEqualityAndHashCode() {
        final HashSet<String> set = new HashSet<>();
        set.add("tag1");
        set.add("tag2");

        final URI imageURI = URI.create("http://edmondfaithbible.com/?page_id=2743&show&file_name=2015_0621%20Fathers%20Day%20Exodus%2020_12.mp3");
        final SeriesModel seriesModel = new SeriesModel(7, "SomeTitle", "slug", Optional.of(imageURI), Optional.of(set));
        final Series series = new Series(seriesModel);
        final Series series2 = new Series(seriesModel);

        assertThat(series.hashCode(), is(series2.hashCode()));
        assertThat(series, is(series2));
    }
}
