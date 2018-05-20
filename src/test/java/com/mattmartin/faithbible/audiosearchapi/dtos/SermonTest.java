package com.mattmartin.faithbible.audiosearchapi.dtos;

import com.mattmartin.faithbible.audiosearchapi.config.FaithDateTimeFormatter;
import com.mattmartin.faithbible.audiosearchapi.models.SermonDocumentModel;
import com.mattmartin.faithbible.audiosearchapi.models.SermonMediaModel;
import org.junit.Test;

import java.net.URI;
import java.util.Optional;

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

        final SermonDocumentModel manual =
                new SermonDocumentModel(
                        "fakeId",
                        "Exodus 20:12 How to Make Your Father's Day on Father's Day MH-FBC SunAM 6/21/2015",
                        "http://edmondfaithbible.com/?page_id=2743&show&file_name=2015_0621%20Fathers%20Day%20Exodus%2020_12.mp3",
                        "Dr Mark Hitchcock",
                        FaithDateTimeFormatter.getLocalDate("2015-06-21"),
                        "Father's Day",
                        Optional.of("fathersDay123"),
                        Optional.empty(),
                        mediaModel);

        final Sermon sermon = Sermon.fromModel(manual);

        assertThat(sermon.getId(), is(manual.getId()));
        assertThat(sermon.getTitle(), is(manual.getTitle()));
        assertThat(sermon.getDate(), is(manual.getDate()));
        assertThat(sermon.getSeries(), is(manual.getSeries()));
        assertThat(sermon.getSpeaker(), is(manual.getSpeaker()));
        assertThat(sermon.getMp3URI(), is(Optional.of(URI.create(manual.getMedia().getMp3().get()))));
        assertThat(sermon.getPdfURI(), is(Optional.of(URI.create(manual.getMedia().getPdf().get()))));
    }

    @Test
    public void testEquality()
    {
        final SermonMediaModel mediaModel =
                new SermonMediaModel("http://edmondfaithbible.com/?page_id=2743&download&file_name=2015_0621%20Fathers%20Day%20MH-FBC%20SunAM.pdf",
                        "http://edmondfaithbible.com/?page_id=2743&show&file_name=2015_0621%20Fathers%20Day%20Exodus%2020_12.mp3");

        final SermonDocumentModel manual =
                new SermonDocumentModel(
                        "fakeId",
                        "Exodus 20:12 How to Make Your Father's Day on Father's Day MH-FBC SunAM 6/21/2015",
                        "http://edmondfaithbible.com/?page_id=2743&show&file_name=2015_0621%20Fathers%20Day%20Exodus%2020_12.mp3",
                        "Dr Mark Hitchcock",
                        FaithDateTimeFormatter.getLocalDate("2015-06-21"),
                        "Father's Day",
                        Optional.of("fathersDay123"),
                        Optional.empty(),
                        mediaModel);

        final Sermon sermon =  Sermon.fromModel(manual);
        final Sermon sermon2 = Sermon.fromModel(manual);

        assertThat(sermon, equalTo(sermon2));
        assertThat(sermon.hashCode(), equalTo(sermon2.hashCode()));
    }
}
