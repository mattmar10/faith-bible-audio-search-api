package com.mattmartin.faithbible.audiosearchapi.controllers;

import com.mattmartin.faithbible.audiosearchapi.dtos.Sermon;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.models.SermonDocumentModel;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.services.ESSermonService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@CrossOrigin
public class SermonController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ESSermonService searchService;

    @Autowired
    public SermonController(final ESSermonService sService){
        this.searchService = sService;
    }

    @ResponseStatus(OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    @RequestMapping(method = RequestMethod.GET,
            value = "/sermon/{id}",
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Sermon> findById(@PathVariable("id") String id){
        final Optional<SermonDocumentModel> sermonMaybe = searchService.findById(id);

        if(sermonMaybe.isPresent()){
            return new ResponseEntity<Sermon>(Sermon.fromModel(sermonMaybe.get()), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ResponseStatus(OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = String.class),
            @ApiResponse(code = 404, message = "Not Found", response = String.class),
            @ApiResponse(code = 503, message = "Service Unavailable")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/sermons/mostrecent")
    public ResponseEntity<Iterable<Sermon>> findMostRecentSermons(@RequestParam("count") int count){
        logger.info("Finding most recent sermons");


        final Page<SermonDocumentModel> sdms = searchService.findMostRecent(count);


        if(sdms.getTotalElements() == 0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else{
            final List<Sermon> found =
                    sdms.stream()
                            .map(sermonDocumentModel -> Sermon.fromModel(sermonDocumentModel)).collect(Collectors.toList());

            return new ResponseEntity<>(found, HttpStatus.OK);
        }

    }

}
