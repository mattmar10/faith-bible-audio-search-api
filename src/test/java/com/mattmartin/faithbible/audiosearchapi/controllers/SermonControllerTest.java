package com.mattmartin.faithbible.audiosearchapi.controllers;

import com.mattmartin.faithbible.audiosearchapi.config.FaithDateTimeFormatter;
import com.mattmartin.faithbible.audiosearchapi.dtos.Sermon;
import com.mattmartin.faithbible.audiosearchapi.models.SermonDocumentModel;
import com.mattmartin.faithbible.audiosearchapi.models.SermonMediaModel;
import com.mattmartin.faithbible.audiosearchapi.services.ESSermonService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SermonControllerTest {

    @Mock
    ESSermonService esSermonService;

    private SermonController sermonController;

    @Before
    public void setup(){
        initMocks(this);
        sermonController = new SermonController(esSermonService);
    }

    @Test
    public void testShouldReturn(){

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
                        mediaModel);

        when(esSermonService.findById("fakeId")).thenReturn(Optional.of(manual));

        ResponseEntity<Sermon> response = sermonController.findById("fakeId");

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(new Sermon(manual)));
    }

    @Test
    public void testShouldReturn404(){
        when(esSermonService.findById("notFound")).thenReturn(Optional.empty());

        ResponseEntity<Sermon> response = sermonController.findById("notFound");

        assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }
}
