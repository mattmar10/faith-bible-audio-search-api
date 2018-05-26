package com.mattmartin.faithbible.audiosearchapi.dtos;

import com.mattmartin.faithbible.audiosearchapi.config.FaithDateTimeFormatter;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.models.SermonDocumentModel;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.models.SermonMediaModel;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.models.StatsModel;
import org.junit.Test;

import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SermonTest {

    @Test
    public void testSermonDto()
    {
        final SermonMediaModel mediaModel =
                new SermonMediaModel("http://edmondfaithbible.com/?page_id=2743&download&file_name=2015_0621%20Fathers%20Day%20MH-FBC%20SunAM.pdf",
                        "http://edmondfaithbible.com/?page_id=2743&show&file_name=2015_0621%20Fathers%20Day%20Exodus%2020_12.mp3");

        final StatsModel statsModel = new StatsModel(7, 5, 3);
        final Set<String> tags = new HashSet<>(Arrays.asList("Fathers Day", "Exodus"));
        final SermonDocumentModel manual =
                new SermonDocumentModel(
                        5,
                        "Exodus 20:12 How to Make Your Father's Day on Father's Day MH-FBC SunAM 6/21/2015",
                        "slug",
                        "http://edmondfaithbible.com/?page_id=2743&show&file_name=2015_0621%20Fathers%20Day%20Exodus%2020_12.mp3",
                        "Dr Mark Hitchcock",
                        FaithDateTimeFormatter.getLocalDate("2015-06-21"),
                        "Father's Day",
                        mediaModel,
                        Optional.of(5),
                        Optional.of(statsModel),
                        Optional.empty(),
                        Optional.of(tags));

        final Sermon sermon = Sermon.fromModel(manual);
        final Optional<Stats> stats = sermon.getStats();

        assertThat(sermon.getId(), is(manual.getId()));
        assertThat(sermon.getTitle(), is(manual.getTitle()));
        assertThat(sermon.getDate(), is(manual.getDate()));
        assertThat(sermon.getSeries(), is(manual.getSeries()));
        assertThat(sermon.getSlug(), is(manual.getSlug()));
        assertThat(sermon.getSpeaker(), is(manual.getSpeaker()));
        assertThat(sermon.getMp3URI(), is(Optional.of(URI.create(manual.getMedia().getMp3().get()))));
        assertThat(sermon.getPdfURI(), is(Optional.of(URI.create(manual.getMedia().getPdf().get()))));
        assertThat(sermon.getTags().get(), is(tags));
        assertThat(stats.get().getLikes(), is(statsModel.getLikes()));
        assertThat(stats.get().getPlays(), is(statsModel.getPlays()));
        assertThat(stats.get().getShares(), is(statsModel.getShares()));
    }

    @Test
    public void testEquality()
    {
        final SermonMediaModel mediaModel =
                new SermonMediaModel("http://edmondfaithbible.com/?page_id=2743&download&file_name=2015_0621%20Fathers%20Day%20MH-FBC%20SunAM.pdf",
                        "http://edmondfaithbible.com/?page_id=2743&show&file_name=2015_0621%20Fathers%20Day%20Exodus%2020_12.mp3");

        final StatsModel statsModel = new StatsModel(7, 5, 3);
        final Set<String> tags = new HashSet<>(Arrays.asList("Fathers Day", "Exodus"));

        final SermonDocumentModel manual =
                new SermonDocumentModel(
                        5,
                        "Exodus 20:12 How to Make Your Father's Day on Father's Day MH-FBC SunAM 6/21/2015",
                        "slug",
                        "http://edmondfaithbible.com/?page_id=2743&show&file_name=2015_0621%20Fathers%20Day%20Exodus%2020_12.mp3",
                        "Dr Mark Hitchcock",
                        FaithDateTimeFormatter.getLocalDate("2015-06-21"),
                        "Father's Day",
                        mediaModel,
                        Optional.of(5),
                        Optional.of(statsModel),
                        Optional.empty(),
                        Optional.of(tags));

        final Sermon sermon =  Sermon.fromModel(manual);
        final Sermon sermon2 = Sermon.fromModel(manual);

        assertThat(sermon, equalTo(sermon2));
        assertThat(sermon.hashCode(), equalTo(sermon2.hashCode()));
        assertThat(sermon.getTags(), equalTo(sermon2.getTags()));
    }
}
