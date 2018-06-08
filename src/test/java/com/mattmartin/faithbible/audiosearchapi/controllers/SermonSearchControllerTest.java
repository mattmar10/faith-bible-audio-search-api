package com.mattmartin.faithbible.audiosearchapi.controllers;

import com.google.common.collect.Iterables;
import com.mattmartin.faithbible.audiosearchapi.config.FaithDateTimeFormatter;
import com.mattmartin.faithbible.audiosearchapi.dtos.Sermon;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.models.SermonDocumentModel;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.models.SermonMediaModel;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.models.StatsModel;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.services.ESSeriesService;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.services.ESSermonService;
import com.mattmartin.faithbible.audiosearchapi.services.SeriesService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SermonSearchControllerTest {

    @Mock
    ESSermonService esSermonService;

    @Mock
    ESSeriesService esSeriesService;

    @Mock
    SeriesService seriesService;

    private SermonSearchController sermonSearchController;
    private SermonDocumentModel fathersDayModel;
    private SermonDocumentModel fakeModel;

    @Before
    public void setup(){
        initMocks(this);
        sermonSearchController = new SermonSearchController(esSermonService, esSeriesService, seriesService);

        final SermonMediaModel mediaModel =
                new SermonMediaModel("http://edmondfaithbible.com/?page_id=2743&download&file_name=2015_0621%20Fathers%20Day%20MH-FBC%20SunAM.pdf",
                        "http://edmondfaithbible.com/?page_id=2743&show&file_name=2015_0621%20Fathers%20Day%20Exodus%2020_12.mp3");

        final SermonMediaModel mediaModel2 =
                new SermonMediaModel("http://dummyserver.com/some.pdf",
                        "http://dummyserver.com/some.mp3");

        final StatsModel statsModel = new StatsModel(20, 3, 7);
        final StatsModel statsModel2 = new StatsModel(10, 2, 1);
        Set<String> tags = new HashSet<>(Arrays.asList("tag1", "tag2"));
        Set<String> tags2 = new HashSet<>(Arrays.asList("tag3", "tag4"));


        fathersDayModel=
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
                        Optional.of("https://s3.amazonaws.com/faith-bible-data/mp3-images/2014_0323+Revelation+3+1-6+The+Church+of+the+Walking+Dead.mp3.jpg"),
                        Optional.of(tags));

        fakeModel =
                new SermonDocumentModel(
                        7,
                        "Some title",
                        "slug",
                        "http://dummyserver.com/some.mp3",
                        "Dr Mark Hitchcock",
                        FaithDateTimeFormatter.getLocalDate("2015-06-21"),
                        "Some Series",
                        mediaModel2,
                        Optional.of(5),
                        Optional.of("seriesSlug2"),
                        Optional.of(statsModel2),
                        Optional.empty(),
                        Optional.of(tags2));
    }

    @Test
    public void testSearch(){

        final PageImpl<SermonDocumentModel> pageImpl =
                new PageImpl<SermonDocumentModel>(
                        Arrays.asList(new SermonDocumentModel[]{fathersDayModel, fakeModel}),
                        PageRequest.of(0, 10), 2);

        when(esSermonService.findByFreeSearch(
                "FathersDay",
                PageRequest.of(0, 10, Sort.Direction.DESC, "date"))).thenReturn(pageImpl);

        ResponseEntity<Iterable<Sermon>> response = sermonSearchController.search("FathersDay", 0, 10);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertTrue(Iterables.contains(response.getBody(), Sermon.fromModel(fathersDayModel)));
        assertTrue(Iterables.contains(response.getBody(), Sermon.fromModel(fakeModel)));
        assertThat(Iterables.size(response.getBody()), is(2));

    }

    @Test
    public void testSearchSeries(){

        final PageImpl<SermonDocumentModel> pageImpl =
                new PageImpl<SermonDocumentModel>(
                        Arrays.asList(new SermonDocumentModel[]{fathersDayModel}),
                        PageRequest.of(0, 10), 1);

        when(esSermonService.findBySeries(
                "Father's Day",
                PageRequest.of(0, 10, Sort.Direction.DESC, "date"))).thenReturn(pageImpl);

        ResponseEntity<Iterable<Sermon>> response = sermonSearchController.searchSeries("Father's Day", 0, 10);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertTrue(Iterables.contains(response.getBody(), Sermon.fromModel(fathersDayModel)));
        assertThat(Iterables.size(response.getBody()), is(1));
    }

    @Test
    public void testSearchSpeaker(){

        final PageImpl<SermonDocumentModel> pageImpl =
                new PageImpl<SermonDocumentModel>(
                        Arrays.asList(new SermonDocumentModel[]{fathersDayModel}),
                        PageRequest.of(0, 10), 1);

        when(esSermonService.findBySpeaker(
                "Dr Mark Hitchcock",
                PageRequest.of(0, 10, Sort.Direction.DESC, "date"))).thenReturn(pageImpl);

        ResponseEntity<Iterable<Sermon>> response = sermonSearchController.searchSpeaker("Dr Mark Hitchcock", 0, 10);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertTrue(Iterables.contains(response.getBody(), Sermon.fromModel(fathersDayModel)));
        assertThat(Iterables.size(response.getBody()), is(1));
    }

}
