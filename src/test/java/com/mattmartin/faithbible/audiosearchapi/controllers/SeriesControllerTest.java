package com.mattmartin.faithbible.audiosearchapi.controllers;

import com.google.common.collect.Sets;
import com.mattmartin.faithbible.audiosearchapi.config.FaithDateTimeFormatter;
import com.mattmartin.faithbible.audiosearchapi.db.models.SeriesDBModel;
import com.mattmartin.faithbible.audiosearchapi.db.models.SermonDBModel;
import com.mattmartin.faithbible.audiosearchapi.dtos.Series;
import com.mattmartin.faithbible.audiosearchapi.dtos.Sermon;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.models.SeriesModel;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.models.SermonDocumentModel;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.models.StatsModel;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.services.ESSeriesService;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.services.ESSermonService;
import com.mattmartin.faithbible.audiosearchapi.http.FBCApiResponse;
import com.mattmartin.faithbible.audiosearchapi.services.SeriesService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SeriesControllerTest {


    @Mock
    ESSeriesService esSeriesService;

    @Mock
    ESSermonService esSermonService;

    @Mock
    SeriesService seriesService;

    private SeriesController seriesController;


    @Before
    public void setup(){
        initMocks(this);
        seriesController = new SeriesController(esSermonService, esSeriesService, seriesService );
    }
    @Test
    public void testShouldReturn404(){
        when(esSermonService.findById(23)).thenReturn(Optional.empty());

        ResponseEntity<Series> response = seriesController.findById(1);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    public void testFindById(){
        final StatsModel statsModel = new StatsModel(20, 3, 7);
        Set<String> tags = new HashSet<>(Arrays.asList("tag1", "tag2"));


        SermonDBModel sermonDBModel = new SermonDBModel();
        sermonDBModel.setId(7);
        sermonDBModel.setTitle("Exodus 20:12 How to Make Your Father's Day on Father's Day MH-FBC SunAM 6/21/2015");
        sermonDBModel.setSlug("slug");
        sermonDBModel.setSpeaker("speaker");
        sermonDBModel.setDate(LocalDate.now());
        sermonDBModel.setImageUrl("http://edmondfaithbible.com/?page_id=2743&show&file_name=2015_0621%20Fathers%20Day%20Exodus%2020_12.mp3");
        sermonDBModel.setLikes(0);
        sermonDBModel.setPlays(0);
        sermonDBModel.setShares(0);

        final SeriesDBModel seriesDBModel = new SeriesDBModel();
        seriesDBModel.setId(9);
        seriesDBModel.setTitle("test");
        seriesDBModel.setImageURL("http://edmondfaithbible.com/?page_id=2743&show&file_name=2015_0621%20Fathers%20Day%20Exodus%2020_12.mp3");
        seriesDBModel.setSlug("slug");
        seriesDBModel.setSermons(Arrays.asList(sermonDBModel));
        seriesDBModel.setLikes(0);
        seriesDBModel.setPlays(0);
        seriesDBModel.setShares(0);
        when(seriesService.findById(9)).thenReturn(Optional.of(seriesDBModel));



        ResponseEntity<Series> response = seriesController.findById(9);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(new Series(seriesDBModel)));
    }

    @Test
    public void testSearch(){

        final SeriesModel series1 = mock(SeriesModel.class);
        final SeriesModel series2 = mock(SeriesModel.class);

        final SermonDocumentModel sermonDocumentModel = mock(SermonDocumentModel.class);

        final PageImpl<SeriesModel> pageImpl =
                new PageImpl<>(
                        Arrays.asList(new SeriesModel[]{series1, series2}),
                        PageRequest.of(0, 10), 1);

        final PageImpl<SermonDocumentModel> pageImpl2 =
                new PageImpl<>(
                        Arrays.asList(new SermonDocumentModel[]{sermonDocumentModel}),
                        PageRequest.of(0, 10), 1);

        when(series1.getId()).thenReturn(1);
        when(series2.getId()).thenReturn(2);

        when(esSermonService.findBySeriesId(1, PageRequest.of(0, 500))).thenReturn(pageImpl2);
        when(esSermonService.findBySeriesId(2, PageRequest.of(0, 500))).thenReturn(pageImpl2);

        when(esSeriesService.findByFreeSearch(
                "searchTerm",
                PageRequest.of(0, 10))).thenReturn(pageImpl);

        ResponseEntity<FBCApiResponse<Iterable<Series>>> response = seriesController.search("searchTerm", 0, 10);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));

    }

    @Test
    public void testFindAllSeries(){
        SermonDBModel sermonDBModel = new SermonDBModel();
        sermonDBModel.setId(7);
        sermonDBModel.setTitle("Exodus 20:12 How to Make Your Father's Day on Father's Day MH-FBC SunAM 6/21/2015");
        sermonDBModel.setSlug("slug");
        sermonDBModel.setSpeaker("speaker");
        sermonDBModel.setDate(LocalDate.now());
        sermonDBModel.setImageUrl("http://edmondfaithbible.com/?page_id=2743&show&file_name=2015_0621%20Fathers%20Day%20Exodus%2020_12.mp3");
        sermonDBModel.setLikes(0);
        sermonDBModel.setPlays(0);
        sermonDBModel.setShares(0);

        final SeriesDBModel seriesDBModel = new SeriesDBModel();
        seriesDBModel.setId(9);
        seriesDBModel.setTitle("test");
        seriesDBModel.setImageURL("http://edmondfaithbible.com/?page_id=2743&show&file_name=2015_0621%20Fathers%20Day%20Exodus%2020_12.mp3");
        seriesDBModel.setSlug("slug");
        seriesDBModel.setSermons(Arrays.asList(sermonDBModel));
        seriesDBModel.setLikes(0);
        seriesDBModel.setPlays(0);
        seriesDBModel.setShares(0);

        final List<SeriesDBModel> seriesList = Arrays.asList(new SeriesDBModel[]{seriesDBModel});
        when(seriesService.getAll()).thenReturn(seriesList);

        FBCApiResponse<Set<Series>> response = seriesController.getAllSeries();

        verify(seriesService).getAll();
        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        final Set<Series> mapped = Sets.newHashSet(new Series(seriesDBModel));
        assertThat(response.getBody(), equalTo(mapped));

    }
}
