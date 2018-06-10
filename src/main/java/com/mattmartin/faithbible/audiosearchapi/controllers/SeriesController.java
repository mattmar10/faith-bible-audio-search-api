package com.mattmartin.faithbible.audiosearchapi.controllers;

import com.mattmartin.faithbible.audiosearchapi.db.models.SeriesDBModel;
import com.mattmartin.faithbible.audiosearchapi.dtos.Series;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.models.SeriesModel;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.models.SermonDocumentModel;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.services.ESSeriesService;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.services.ESSermonService;
import com.mattmartin.faithbible.audiosearchapi.http.FBCApiResponse;
import com.mattmartin.faithbible.audiosearchapi.services.SeriesService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@CrossOrigin
public class SeriesController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ESSermonService sermonESService;
    private final ESSeriesService seriesESService;
    private final SeriesService seriesService;

    @Autowired
    public SeriesController(final ESSermonService sService,
                            final ESSeriesService esSeriesService,
                            final SeriesService seriesService){
        this.sermonESService = sService;
        this.seriesESService = esSeriesService;
        this.seriesService = seriesService;
    }

    @ResponseStatus(OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    @RequestMapping(method = RequestMethod.GET,
            value = "/series/{id}",
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Series> findById(@PathVariable("id") int id){
        //final Optional<SeriesModel> seriesMaybe = seriesESService.findById(id);
        final Optional<SeriesDBModel> seriesMaybe2 = seriesService.findById(id);

        if(seriesMaybe2.isPresent()){
            return new ResponseEntity<Series>(new Series(seriesMaybe2.get()), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ResponseStatus(OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "BAD REQUEST")
    })
    @RequestMapping(method = RequestMethod.GET,
            value = "/series/search",
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<FBCApiResponse<Iterable<Series>>> search(@RequestParam("q") String query,
                                                   @RequestParam int page,
                                                   @RequestParam int size){

        final Page<SeriesModel> found =
                seriesESService.findByFreeSearch(query, PageRequest.of(page, size));

        final Iterable<Series> mapped = found.map(seriesModel -> {

            final Page<SermonDocumentModel> sermons =
                    sermonESService.findBySeriesId(seriesModel.getId(), PageRequest.of(0, 500));

            seriesModel.setSermons(sermons.getContent());

            return new Series(seriesModel);
        });
        final FBCApiResponse<Iterable<Series>> response = new FBCApiResponse<>(mapped, HttpStatus.OK);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ResponseStatus(OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    @RequestMapping(method = RequestMethod.GET,
            value = "/series/slug/{slug}",
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<FBCApiResponse<Series>> findBySlug(@PathVariable("slug") String slug){
        final Optional<SeriesDBModel> seriesMaybe2 = seriesService.findBySlug(slug);

        if(seriesMaybe2.isPresent()){
            final Series series = new Series(seriesMaybe2.get());
            return new ResponseEntity<>(new FBCApiResponse<Series>(series, HttpStatus.OK), HttpStatus.OK);
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

        final List<SeriesModel> results = seriesESService.findMostRecentSeries(count);

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

    @ResponseStatus(CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "CREATED", response = String.class),
            @ApiResponse( code = 200, message = "UPDATED" ),
            @ApiResponse(code = 400, message = "Bad Request", response = String.class),
            @ApiResponse(code = 503, message = "Service Unavailable")
    })
    @RequestMapping(method = RequestMethod.POST,
            value = "/series",
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<FBCApiResponse<Series>> create(@RequestBody SeriesDBModel seriesDBModel){

        FBCApiResponse<Series> response;
        if(seriesDBModel.getId() != null && seriesService.findById(seriesDBModel.getId()).isPresent()){
            logger.error("Can't create an entity that already exists");

            response = new FBCApiResponse<Series>(null,"Can't create an entity that already exists" );
        }
        else{
            logger.info(String.format("Saving or updating series %s", seriesDBModel));
            seriesDBModel.setCreatedDate(LocalDate.now());
            seriesDBModel.setLastUpdatedDate(LocalDate.now());
            final SeriesDBModel persisted = this.seriesService.save(seriesDBModel);

            //persisted.getSermons().forEach(sermon -> sermon.setSeries(persisted));
            final Series series = new Series(persisted);

            response = new FBCApiResponse<Series>(series, HttpStatus.OK);
        }

        return new ResponseEntity<FBCApiResponse<Series>>(response, response.getStatusCode());
    }

    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "CREATED", response = String.class),
            @ApiResponse( code = 200, message = "UPDATED" ),
            @ApiResponse(code = 400, message = "Bad Request", response = String.class),
            @ApiResponse(code = 503, message = "Service Unavailable")
    })
    @RequestMapping(method = RequestMethod.PUT,
            value = "/series/{id}",
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Series> update(@PathVariable("id") int id,
                                         @RequestBody SeriesDBModel seriesDBModel){

        logger.info(String.format("Saving or updating series %s", seriesDBModel));
        seriesDBModel.setLastUpdatedDate(LocalDate.now());
        final SeriesDBModel persisted = this.seriesService.save(seriesDBModel);

        final Series series = new Series(persisted);

        return new ResponseEntity<>(series, HttpStatus.OK);
    }


}
