package com.mattmartin.faithbible.audiosearchapi.controllers;

import com.mattmartin.faithbible.audiosearchapi.dtos.Sermon;
import com.mattmartin.faithbible.audiosearchapi.services.ESSermonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

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
    public Sermon search(@RequestParam("q") String query){
        final String url = "https://search-faithbibleaudio-test-o7t56t5553l4m33riniuxvw6nq.us-east-1.es.amazonaws.com/audio_data/sermons/_search?";
        final RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> result = restTemplate.getForEntity(url + "q="+ query, String.class);

        return new Sermon("id", "Test Sermon",  "speaker", "series");
    }

    @RequestMapping(method = RequestMethod.GET,
            value = "/search/series",
            produces = APPLICATION_JSON_VALUE)
    public Iterable<Sermon> searchSeries(@RequestParam("q") String query){

        return searchService.findBySeries(query, PageRequest.of(0, 10))
                .map(s -> new Sermon(s.getId(), s.getTitle(), s.getSpeaker(), s.getSeries()));
    }

    @RequestMapping(method = RequestMethod.GET,
            value = "/search/speaker",
            produces = APPLICATION_JSON_VALUE)
    public List<Sermon> searchSpeaker(@RequestParam("q") String query){

        return Collections.emptyList();

    }


}
