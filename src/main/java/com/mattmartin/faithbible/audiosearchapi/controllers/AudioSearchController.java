package com.mattmartin.faithbible.audiosearchapi.controllers;

import com.mattmartin.faithbible.audiosearchapi.dtos.Sermon;
import com.mattmartin.faithbible.audiosearchapi.models.AudioJsonSource;
import com.mattmartin.faithbible.audiosearchapi.models.SermonDocumentModel;
import com.mattmartin.faithbible.audiosearchapi.services.ESSermonService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class AudioSearchController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ESSermonService searchService;

    @Autowired
    public AudioSearchController(final ESSermonService sService){
        this.searchService = sService;
    }

//    @RequestMapping(method = RequestMethod.GET,
//            value = "/search",
//            produces = APPLICATION_JSON_VALUE)
//    public ResponseEntity<Sermon> search(@RequestParam("q") String query, @RequestParam int page, @RequestParam int size){
//        final String url = "https://search-faithbibleaudio-test-o7t56t5553l4m33riniuxvw6nq.us-east-1.es.amazonaws.com/audio_data/sermons/_search?";
//        final RestTemplate restTemplate = new RestTemplate();
//
//        ResponseEntity<String> result = restTemplate.getForEntity(url + "q="+ query, String.class);
//
//        return new ResponseEntity<>(new Sermon(), HttpStatus.OK);
//    }

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

        return searchService.findBySeries(query, PageRequest.of(page, size, Sort.Direction.DESC, "date"))
                .map(s -> new Sermon(s));
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

        return searchService.findBySpeaker(query, PageRequest.of(page, size, Sort.Direction.DESC, "date"))
                .map(s -> new Sermon(s));

    }

    @ResponseStatus(OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = String.class),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 503, message = "Service Unavailable")
    })
    @RequestMapping(method = RequestMethod.POST, value = "/refreshData")
    public void refreshData(@RequestBody AudioJsonSource audioJsonSource){
        logger.info(String.format("Refreshing data from source %s", audioJsonSource));

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<SermonDocumentModel[]> response =
                restTemplate.getForEntity(audioJsonSource.getUrlToJson(), SermonDocumentModel[].class);

        logger.info("Response from data source " +response.getStatusCode());

        Arrays.stream(response.getBody()).parallel().forEach(sermon -> {

            try{
                final String id = getMD5(sermon);
                sermon.setId(id);
                searchService.save(sermon);
            }
            catch (Exception e){
                logger.error("Unable to persist sermon", e);
            }

        });

    }

    private String getMD5(final SermonDocumentModel sermon) throws NoSuchAlgorithmException {


        final MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(sermon.getTitle().getBytes());

        byte byteData[] = md.digest();

        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }


}
