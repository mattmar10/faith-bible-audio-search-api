package com.mattmartin.faithbible.audiosearchapi.controllers;

import com.google.common.collect.Iterables;
import com.mattmartin.faithbible.audiosearchapi.config.FaithDateTimeFormatter;
import com.mattmartin.faithbible.audiosearchapi.db.models.SeriesDBModel;
import com.mattmartin.faithbible.audiosearchapi.db.models.SermonDBModel;
import com.mattmartin.faithbible.audiosearchapi.dtos.Sermon;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.models.SermonDocumentModel;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.models.SermonMediaModel;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.models.StatsModel;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.services.ESSermonService;
import com.mattmartin.faithbible.audiosearchapi.http.FBCApiResponse;
import com.mattmartin.faithbible.audiosearchapi.services.SermonsService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SermonControllerTest {

    @Mock
    ESSermonService esSermonService;

    @Mock
    SermonsService sermonService;

    private SermonController sermonController;

    private SeriesDBModel seriesDBModel;
    private SermonDBModel sermonDBModel;

    @Before
    public void setup(){
        initMocks(this);
        sermonController = new SermonController(esSermonService, sermonService);

        seriesDBModel = new SeriesDBModel();
        seriesDBModel.setId(5);
        seriesDBModel.setTitle("test123");
        seriesDBModel.setImageURL("http://edmondfaithbible.com/?page_id=2743&show&file_name=2015_0621%20Fathers%20Day%20Exodus%2020_12.mp3");
        seriesDBModel.setSlug("slug");
        seriesDBModel.setLikes(0);
        seriesDBModel.setPlays(0);
        seriesDBModel.setShares(0);

        sermonDBModel = new SermonDBModel();
        sermonDBModel.setId(7);
        sermonDBModel.setTitle("Exodus 20:12 How to Make Your Father's Day on Father's Day MH-FBC SunAM 6/21/2015");
        sermonDBModel.setSeries(seriesDBModel);
        sermonDBModel.setSlug("slug");
        sermonDBModel.setSpeaker("speaker");
        sermonDBModel.setDate(LocalDate.now());
        sermonDBModel.setImageUrl("http://edmondfaithbible.com/?page_id=2743&show&file_name=2015_0621%20Fathers%20Day%20Exodus%2020_12.mp3");
        sermonDBModel.setLikes(0);
        sermonDBModel.setPlays(0);
        sermonDBModel.setShares(0);
        seriesDBModel.setSermons(Arrays.asList(sermonDBModel));
    }

    @Test
    public void testShouldReturn(){

        final SermonMediaModel mediaModel =
                new SermonMediaModel("http://edmondfaithbible.com/?page_id=2743&download&file_name=2015_0621%20Fathers%20Day%20MH-FBC%20SunAM.pdf",
                        "http://edmondfaithbible.com/?page_id=2743&show&file_name=2015_0621%20Fathers%20Day%20Exodus%2020_12.mp3");

        final StatsModel statsModel = new StatsModel(20, 3, 7);
        Set<String> tags = new HashSet<>(Arrays.asList("tag1", "tag2"));

        final SeriesDBModel seriesDBModel = new SeriesDBModel();
        seriesDBModel.setId(9);
        seriesDBModel.setTitle("test");
        seriesDBModel.setImageURL("http://edmondfaithbible.com/?page_id=2743&show&file_name=2015_0621%20Fathers%20Day%20Exodus%2020_12.mp3");
        seriesDBModel.setSlug("slug");
        seriesDBModel.setLikes(0);
        seriesDBModel.setPlays(0);
        seriesDBModel.setShares(0);

        SermonDBModel sermonDBModel = new SermonDBModel();
        sermonDBModel.setId(7);
        sermonDBModel.setTitle("Exodus 20:12 How to Make Your Father's Day on Father's Day MH-FBC SunAM 6/21/2015");
        sermonDBModel.setSeries(seriesDBModel);
        sermonDBModel.setSlug("slug");
        sermonDBModel.setSpeaker("speaker");
        sermonDBModel.setDate(LocalDate.now());
        sermonDBModel.setImageUrl("http://edmondfaithbible.com/?page_id=2743&show&file_name=2015_0621%20Fathers%20Day%20Exodus%2020_12.mp3");
        sermonDBModel.setLikes(0);
        sermonDBModel.setPlays(0);
        sermonDBModel.setShares(0);
        seriesDBModel.setSermons(Arrays.asList(sermonDBModel));


        when(sermonService.findById(5)).thenReturn(Optional.of(sermonDBModel));

        ResponseEntity<FBCApiResponse<Sermon>> response = sermonController.findById(5);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody().getBody(), equalTo(Sermon.fromDBModel(sermonDBModel)));
    }

    @Test
    public void testShouldReturn404(){
        when(esSermonService.findById(-1)).thenReturn(Optional.empty());

        ResponseEntity<FBCApiResponse<Sermon>> response = sermonController.findById(8);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    public void testMostRecent(){
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
                        Optional.of("https://s3.amazonaws.com/faith-bible-data/mp3-images/2014_0323+Revelation+3+1-6+The+Church+of+the+Walking+Dead.mp3.jpg"),
                        Optional.of(tags));

        final SermonDocumentModel manual2 =
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
                        Optional.of(statsModel2),
                        Optional.empty(),
                        Optional.of(tags2));

        final PageImpl<SermonDocumentModel> pageImpl =
                new PageImpl<SermonDocumentModel>(
                        Arrays.asList(new SermonDocumentModel[]{manual, manual2}),
                        PageRequest.of(0, 10), 2);

        when(esSermonService.findMostRecent(2)).thenReturn(pageImpl);
        ResponseEntity<Iterable<Sermon>> response = sermonController.findMostRecentSermons(2);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertTrue(Iterables.contains(response.getBody(), Sermon.fromModel(manual)));
        assertTrue(Iterables.contains(response.getBody(), Sermon.fromModel(manual2)));
        assertThat(Iterables.size(response.getBody()), is(2));
    }

    @Test
    public void testLikeCount(){

        when(sermonService.findById(7)).thenReturn(Optional.of(sermonDBModel));
        when(sermonService.save(sermonDBModel)).thenReturn(sermonDBModel);
        ResponseEntity<FBCApiResponse<Sermon>> response = sermonController.incrementLikeCount(7);

        assertThat(response.getBody().getBody().getStats().get().getLikes(), is(Optional.of(1)));

    }

    @Test
    public void testPlayCount(){

        when(sermonService.findById(7)).thenReturn(Optional.of(sermonDBModel));
        when(sermonService.save(sermonDBModel)).thenReturn(sermonDBModel);
        ResponseEntity<FBCApiResponse<Sermon>> response = sermonController.incrementPlayCount(7);

        assertThat(response.getBody().getBody().getStats().get().getPlays(), is(Optional.of(1)));

    }

    @Test
    public void testShareCount(){

        when(sermonService.findById(7)).thenReturn(Optional.of(sermonDBModel));
        when(sermonService.save(sermonDBModel)).thenReturn(sermonDBModel);
        ResponseEntity<FBCApiResponse<Sermon>> response = sermonController.incrementShareCount(7);

        assertThat(response.getBody().getBody().getStats().get().getShares(), is(Optional.of(1)));

    }
}
