package com.mattmartin.faithbible.audiosearchapi.controllers;

import com.mattmartin.faithbible.audiosearchapi.dtos.Sermon;
import com.mattmartin.faithbible.audiosearchapi.models.SermonDocumentModel;
import com.mattmartin.faithbible.audiosearchapi.services.ESSermonService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class SermonController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ESSermonService searchService;

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
            return new ResponseEntity<Sermon>(new Sermon(sermonMaybe.get()), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
