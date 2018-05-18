package com.mattmartin.faithbible.audiosearchapi.controllers;

import com.mattmartin.faithbible.audiosearchapi.dtos.Series;
import com.mattmartin.faithbible.audiosearchapi.dtos.Sermon;
import com.mattmartin.faithbible.audiosearchapi.models.SeriesModel;
import com.mattmartin.faithbible.audiosearchapi.models.SermonDocumentModel;
import com.mattmartin.faithbible.audiosearchapi.services.ESSeriesService;
import com.mattmartin.faithbible.audiosearchapi.services.ESSermonService;
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
public class SeriesController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ESSermonService searchService;
    private final ESSeriesService seriesService;

    @Autowired
    public SeriesController(final ESSermonService sService, final ESSeriesService esSeriesService){
        this.searchService = sService;
        this.seriesService = esSeriesService;
    }

    @ResponseStatus(OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    @RequestMapping(method = RequestMethod.GET,
            value = "/series/{id}",
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Series> findById(@PathVariable("id") String id){
        final Optional<SeriesModel> seriesMaybe = seriesService.findById(id);

        if(seriesMaybe.isPresent()){
            return new ResponseEntity<Series>(new Series(seriesMaybe.get()), HttpStatus.OK);
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
    @RequestMapping(method = RequestMethod.GET, value = "/series/mostrecent")
    public ResponseEntity<Iterable<Series>> findMostRecentSeries(@RequestParam("count") int count){
        logger.info("Finding most recent series");

        final List<SeriesModel> results = seriesService.findMostRecentSeries(count);

        if(results.size() == 0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else{
            final List<Series> found =
                    results.stream()
                            .map(seriesModel -> new Series(seriesModel)).collect(Collectors.toList());

            return new ResponseEntity<>(found, HttpStatus.OK);
        }

    }
}
