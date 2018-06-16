package com.mattmartin.faithbible.audiosearchapi.controllers;

import com.mattmartin.faithbible.audiosearchapi.db.models.SeriesDBModel;
import com.mattmartin.faithbible.audiosearchapi.db.models.SermonDBModel;
import com.mattmartin.faithbible.audiosearchapi.dtos.Series;
import com.mattmartin.faithbible.audiosearchapi.dtos.Sermon;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.models.SermonDocumentModel;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.services.ESSermonService;
import com.mattmartin.faithbible.audiosearchapi.http.FBCApiResponse;
import com.mattmartin.faithbible.audiosearchapi.services.SermonsService;
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
    private final SermonsService sermonsService;

    @Autowired
    public SermonController(final ESSermonService sService,
                            final SermonsService sermonsService)
    {
        this.searchService = sService;
        this.sermonsService = sermonsService;
    }

    @ResponseStatus(OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    @RequestMapping(method = RequestMethod.GET,
            value = "/sermon/{id}",
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<FBCApiResponse<Sermon>> findById(@PathVariable("id") Integer id){
        final Optional<SermonDBModel> sermonMaybe = sermonsService.findById(id);

        if(sermonMaybe.isPresent()){
            final FBCApiResponse<Sermon> response =
                    new FBCApiResponse<Sermon>(Sermon.fromDBModel(sermonMaybe.get()), HttpStatus.OK);
            return new ResponseEntity<>(response, response.getStatusCode());

        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ResponseStatus(OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    @RequestMapping(method = RequestMethod.PUT,
            value = "/sermon/{id}/like",
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<FBCApiResponse<Sermon>> incrementLikeCount(@PathVariable("id") Integer id){
        final Optional<SermonDBModel> sermonMaybe = sermonsService.findById(id);

        if(sermonMaybe.isPresent()){
            final SermonDBModel sermonDBModel = sermonMaybe.get();
            sermonDBModel.setLikes(sermonDBModel.getLikes() + 1);

            final SermonDBModel saved = sermonsService.save(sermonDBModel);
            searchService.save(SermonDocumentModel.fromSermonDBModel(saved));

            final FBCApiResponse<Sermon> response =
                    new FBCApiResponse<Sermon>(Sermon.fromDBModel(saved), HttpStatus.OK);

            return new ResponseEntity<>(response, response.getStatusCode());
        }

        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ResponseStatus(OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    @RequestMapping(method = RequestMethod.PUT,
            value = "/sermon/{id}/play",
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<FBCApiResponse<Sermon>> incrementPlayCount(@PathVariable("id") Integer id){
        final Optional<SermonDBModel> sermonMaybe = sermonsService.findById(id);

        if(sermonMaybe.isPresent()){
            final SermonDBModel sermonDBModel = sermonMaybe.get();
            sermonDBModel.setPlays(sermonDBModel.getPlays() + 1);

            final SermonDBModel saved = sermonsService.save(sermonDBModel);
            searchService.save(SermonDocumentModel.fromSermonDBModel(saved));

            final FBCApiResponse<Sermon> response =
                    new FBCApiResponse<Sermon>(Sermon.fromDBModel(saved), HttpStatus.OK);

            return new ResponseEntity<>(response, response.getStatusCode());
        }

        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ResponseStatus(OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    @RequestMapping(method = RequestMethod.PUT,
            value = "/sermon/{id}/share",
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<FBCApiResponse<Sermon>> incrementShareCount(@PathVariable("id") Integer id){
        final Optional<SermonDBModel> sermonMaybe = sermonsService.findById(id);

        if(sermonMaybe.isPresent()){
            final SermonDBModel sermonDBModel = sermonMaybe.get();
            sermonDBModel.setShares(sermonDBModel.getShares() + 1);

            final SermonDBModel saved = sermonsService.save(sermonDBModel);
            searchService.save(SermonDocumentModel.fromSermonDBModel(saved));

            final FBCApiResponse<Sermon> response =
                    new FBCApiResponse<Sermon>(Sermon.fromDBModel(saved), HttpStatus.OK);

            return new ResponseEntity<>(response, response.getStatusCode());
        }

        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @ResponseStatus(OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    @RequestMapping(method = RequestMethod.GET,
            value = "/sermon/slug/{slug}",
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<FBCApiResponse<Sermon>> findBySlug(@PathVariable("slug") String slug){
        final Optional<SermonDBModel> sermonDBModelMaybe = sermonsService.findBySlug(slug);

        if(sermonDBModelMaybe.isPresent()){
            final Sermon sermon = Sermon.fromDBModel(sermonDBModelMaybe.get());
            return new ResponseEntity<>(new FBCApiResponse<>(sermon, HttpStatus.OK), HttpStatus.OK);
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
