package com.mattmartin.faithbible.audiosearchapi.dtos;

import com.mattmartin.faithbible.audiosearchapi.config.FaithDateTimeFormatter;
import com.mattmartin.faithbible.audiosearchapi.db.models.SeriesDBModel;
import com.mattmartin.faithbible.audiosearchapi.db.models.SermonDBModel;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.models.SermonDocumentModel;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.models.SermonMediaModel;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.models.StatsModel;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
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
                        Optional.of("seriesSLug"),
                        Optional.of(statsModel),
                        Optional.empty(),
                        Optional.of(tags),
                        Optional.of(true));

        final Sermon sermon = Sermon.fromModel(manual);
        final Optional<Stats> stats = sermon.getStats();

        assertThat(sermon.getId(), is(manual.getId()));
        assertThat(sermon.getTitle(), is(manual.getTitle()));
        assertThat(sermon.getDate(), is(manual.getDate()));
        assertThat(sermon.getSeries(), is(manual.getSeries()));
        assertThat(sermon.getSlug(), is(manual.getSlug()));
        assertThat(sermon.getSeriesSlug(), is(manual.getSeriesSlug()));
        assertThat(sermon.getSpeaker(), is(manual.getSpeaker()));
        assertThat(sermon.getMp3URI(), is(Optional.of(URI.create(manual.getMedia().getMp3().get()))));
        assertThat(sermon.getPdfURI(), is(Optional.of(URI.create(manual.getMedia().getPdf().get()))));
        assertThat(sermon.getTags().get(), is(tags));
        assertThat(stats.get().getLikes(), is(statsModel.getLikes()));
        assertThat(stats.get().getPlays(), is(statsModel.getPlays()));
        assertThat(stats.get().getShares(), is(statsModel.getShares()));
        assertThat(sermon.getSanitized(), equalTo(manual.getSanitized()));
    }

    @Test
    public void testFromDBModel() throws URISyntaxException {
        final SeriesDBModel seriesDBModel = new SeriesDBModel();
        seriesDBModel.setId(9);
        seriesDBModel.setTitle("test");
        seriesDBModel.setImageURL("http://edmondfaithbible.com/?page_id=2743&show&file_name=2015_0621%20Fathers%20Day%20Exodus%2020_12.mp3");
        seriesDBModel.setSlug("slug");
        seriesDBModel.setLikes(0);
        seriesDBModel.setPlays(0);
        seriesDBModel.setShares(0);

        final SermonDBModel sermonDBModel = new SermonDBModel();
        sermonDBModel.setId(7);
        sermonDBModel.setTitle("Exodus 20:12 How to Make Your Father's Day on Father's Day MH-FBC SunAM 6/21/2015");
        sermonDBModel.setSeries(seriesDBModel);
        sermonDBModel.setSlug("slug");
        sermonDBModel.setSpeaker("speaker");
        sermonDBModel.setDate(LocalDate.now());
        sermonDBModel.setPdfUrl("http://pdf.pdf");
        sermonDBModel.setMp3Url("http://somethinghere.mp3");
        sermonDBModel.setImageUrl("http://edmondfaithbible.com/?page_id=2743&show&file_name=2015_0621%20Fathers%20Day%20Exodus%2020_12.mp3");
        sermonDBModel.setLikes(0);
        sermonDBModel.setPlays(0);
        sermonDBModel.setShares(0);
        sermonDBModel.setMapped(false);

        final Sermon fromDB = Sermon.fromDBModel(sermonDBModel);

        assertThat(fromDB.getId(), equalTo(sermonDBModel.getId()));
        assertThat(fromDB.getTitle(), equalTo(sermonDBModel.getTitle()));
        assertThat(fromDB.getSanitized(), equalTo(Optional.of(sermonDBModel.getMapped())));
        assertThat(fromDB.getSeries(), equalTo(seriesDBModel.getTitle()));
        assertThat(fromDB.getSlug(), equalTo(sermonDBModel.getSlug()));

        assertThat(fromDB.getMp3URI(), equalTo(Optional.of(new URI(sermonDBModel.getMp3Url()))));
        assertThat(fromDB.getImageURI(), equalTo(Optional.of(new URI(sermonDBModel.getImageUrl()))));
        assertThat(fromDB.getPdfURI(), equalTo(Optional.of(new URI(sermonDBModel.getPdfUrl()))));

        final Stats stats = fromDB.getStats().get();
        assertThat(stats.getLikes().get(), equalTo(sermonDBModel.getLikes()));
        assertThat(stats.getPlays().get(), equalTo(sermonDBModel.getPlays()));
        assertThat(stats.getShares().get(), equalTo(sermonDBModel.getShares()));



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
                        Optional.of("seriesSlug"),
                        Optional.of(statsModel),
                        Optional.empty(),
                        Optional.of(tags),
                        Optional.of(true));

        final Sermon sermon =  Sermon.fromModel(manual);
        final Sermon sermon2 = Sermon.fromModel(manual);

        assertThat(sermon, equalTo(sermon2));
        assertThat(sermon.hashCode(), equalTo(sermon2.hashCode()));
        assertThat(sermon.getTags(), equalTo(sermon2.getTags()));
    }
}
