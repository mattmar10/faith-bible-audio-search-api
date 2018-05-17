package com.mattmartin.faithbible.audiosearchapi.controllers;

import com.mattmartin.faithbible.audiosearchapi.config.FaithDateTimeFormatter;
import com.mattmartin.faithbible.audiosearchapi.dtos.Series;
import com.mattmartin.faithbible.audiosearchapi.dtos.Sermon;
import com.mattmartin.faithbible.audiosearchapi.models.SeriesModel;
import com.mattmartin.faithbible.audiosearchapi.models.SermonDocumentModel;
import com.mattmartin.faithbible.audiosearchapi.services.ESSeriesService;
import com.mattmartin.faithbible.audiosearchapi.services.ESSermonService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SeriesControllerTest {


    @Mock
    ESSeriesService esSeriesService;

    @Mock
    ESSermonService esSermonService;

    private SeriesController seriesController;

    @Before
    public void setup(){
        initMocks(this);
        seriesController = new SeriesController(esSermonService, esSeriesService);
    }
    @Test
    public void testShouldReturn404(){
        when(esSermonService.findById("notFound")).thenReturn(Optional.empty());

        ResponseEntity<Series> response = seriesController.findById("notFound");

        assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    public void testFindById(){
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
                        null);

        final SeriesModel seriesModel = new SeriesModel("id", "title", Optional.empty());

        when(esSeriesService.findById("id")).thenReturn(Optional.of(seriesModel));

        ResponseEntity<Series> response = seriesController.findById("id");

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(new Series(seriesModel)));
    }
}
