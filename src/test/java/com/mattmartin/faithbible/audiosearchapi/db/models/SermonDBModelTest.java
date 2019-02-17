package com.mattmartin.faithbible.audiosearchapi.db.models;

import org.junit.Test;

import java.time.LocalDate;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class SermonDBModelTest {

    @Test
    public void testUpdate(){

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
        sermonDBModel.setImageUrl("http://edmondfaithbible.com/?page_id=2743&show&file_name=2015_0621%20Fathers%20Day%20Exodus%2020_12.mp3");
        sermonDBModel.setLikes(0);
        sermonDBModel.setPlays(0);
        sermonDBModel.setShares(0);
        sermonDBModel.setMapped(false);

        SermonDBModel sermonDBModel2 = new SermonDBModel();
        sermonDBModel2.setId(7);
        sermonDBModel2.setTitle("title22");
        sermonDBModel2.setSeries(seriesDBModel);
        sermonDBModel2.setSlug("slug2");
        sermonDBModel2.setSpeaker("speaker2");
        sermonDBModel.setPdfUrl("http://pdf2.pdf");
        sermonDBModel2.setDate(LocalDate.now());
        sermonDBModel2.setImageUrl("http://mp3.mp3");
        sermonDBModel2.setLikes(5);
        sermonDBModel2.setPlays(6);
        sermonDBModel2.setShares(7);
        sermonDBModel2.setMapped(true);

        final SermonDBModel updated = sermonDBModel.updateFromOtherSermonDBModel(sermonDBModel2);

        assertThat(updated, equalTo(sermonDBModel2));
        assertThat(sermonDBModel2.getMapped(), equalTo(true));
    }
}