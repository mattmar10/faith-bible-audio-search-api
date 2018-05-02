package com.mattmartin.faithbible.audiosearchapi.controllers;

import com.mattmartin.faithbible.audiosearchapi.dtos.Sermon;
import com.mattmartin.faithbible.audiosearchapi.models.AudioJsonSource;
import com.mattmartin.faithbible.audiosearchapi.services.ESSermonService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class AudioSearchController {

    private final ESSermonService searchService;

    @Autowired
    public AudioSearchController(final ESSermonService sService){
        this.searchService = sService;
    }

    @RequestMapping(method = RequestMethod.GET,
            value = "/search",
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Sermon> search(@RequestParam("q") String query, @RequestParam int page, @RequestParam int size){
        final String url = "https://search-faithbibleaudio-test-o7t56t5553l4m33riniuxvw6nq.us-east-1.es.amazonaws.com/audio_data/sermons/_search?";
        final RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> result = restTemplate.getForEntity(url + "q="+ query, String.class);

        return new ResponseEntity<>(new Sermon("id", "Test Sermon",  "speaker", "series"), HttpStatus.OK);
    }

    @ResponseStatus(OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "BAD REQUEST")
    })
    @RequestMapping(method = RequestMethod.GET,
            value = "/search/series",
            produces = APPLICATION_JSON_VALUE)
    public Iterable<Sermon> searchSeries(
            @RequestParam("q") String query,
            @RequestParam int page,
            @RequestParam int size){

        return searchService.findBySeries(query, PageRequest.of(page, size))
                .map(s -> new Sermon(s.getId(), s.getTitle(), s.getSpeaker(), s.getSeries()));
    }

    @ResponseStatus(OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "BAD REQUEST")
    })
    @RequestMapping(method = RequestMethod.GET,
            value = "/search/speaker",
            produces = APPLICATION_JSON_VALUE)
    public Iterable<Sermon> searchSpeaker(
            @RequestParam("q") String query,
            @RequestParam int page,
            @RequestParam int size){

        return searchService.findBySpeaker(query, PageRequest.of(page, size))
                .map(s -> new Sermon(s.getId(), s.getTitle(), s.getSpeaker(), s.getSeries()));

    }

    @ResponseStatus(OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = String.class),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 503, message = "Service Unavailable")
    })
    @RequestMapping(method = RequestMethod.POST, value = "/refreshData")
    public void refreshData(@RequestBody AudioJsonSource audioJsonSource){

    }


}
