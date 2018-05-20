package com.mattmartin.faithbible.audiosearchapi.controllers;

import com.mattmartin.faithbible.audiosearchapi.dtos.Sermon;
import com.mattmartin.faithbible.audiosearchapi.models.AudioJsonSource;
import com.mattmartin.faithbible.audiosearchapi.models.SeriesModel;
import com.mattmartin.faithbible.audiosearchapi.models.SermonDocumentModel;
import com.mattmartin.faithbible.audiosearchapi.services.ESSeriesService;
import com.mattmartin.faithbible.audiosearchapi.services.ESSermonService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.http.client.utils.URLEncodedUtils;
import org.hibernate.validator.internal.constraintvalidators.hv.URLValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@CrossOrigin
@RestController
public class SermonSearchController {

    private static final Logger logger = LoggerFactory.getLogger(SermonSearchController.class);

    private final ESSermonService searchService;
    private final ESSeriesService seriesService;

    @Autowired
    public SermonSearchController(final ESSermonService sService,
                                  final ESSeriesService eSeriesService){
        this.searchService = sService;
        this.seriesService = eSeriesService;
    }

    @ResponseStatus(OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "BAD REQUEST")
    })
    @RequestMapping(method = RequestMethod.GET,
            value = "/search",
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Sermon>> search(@RequestParam("q") String query,
                                                   @RequestParam int page,
                                                   @RequestParam int size){

        final Page<SermonDocumentModel> found =
                searchService.findByFreeSearch(query, PageRequest.of(page, size, Sort.Direction.DESC, "date"));

        final Iterable<Sermon> mapped = found.map(sermonDocumentModel -> Sermon.fromModel(sermonDocumentModel));

        return new ResponseEntity<Iterable<Sermon>>(mapped, HttpStatus.OK);
    }

    @ResponseStatus(OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "BAD REQUEST")
    })
    @RequestMapping(method = RequestMethod.GET,
            value = "/search/series",
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Sermon>> searchSeries(
            @RequestParam("q") String query,
            @RequestParam int page,
            @RequestParam int size){

         final Page<SermonDocumentModel> found =
                 searchService.findBySeries(query, PageRequest.of(page, size, Sort.Direction.DESC, "date"));

        final Iterable<Sermon> mapped = found.map(sermonDocumentModel -> Sermon.fromModel(sermonDocumentModel));

        return new ResponseEntity<Iterable<Sermon>>(mapped, HttpStatus.OK);
    }

    @ResponseStatus(OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "BAD REQUEST")
    })
    @RequestMapping(method = RequestMethod.GET,
            value = "/search/speaker",
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Sermon>> searchSpeaker(
            @RequestParam("q") String query,
            @RequestParam int page,
            @RequestParam int size){

        final Page<SermonDocumentModel> found =
                searchService.findBySpeaker(query, PageRequest.of(page, size, Sort.Direction.DESC, "date"));

        final Iterable<Sermon> mapped = found.map(sermonDocumentModel -> Sermon.fromModel(sermonDocumentModel));

        return new ResponseEntity<Iterable<Sermon>>(mapped, HttpStatus.OK);
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

        Map<String, SeriesModel> seriesMap = new HashMap<>();

        Arrays.stream(response.getBody()).forEach(sermon -> {

            sermon.getImage().ifPresent(s -> {

                String encoded = convertToURLEscapingIllegalCharacters(s);

                sermon.setImage(Optional.ofNullable(encoded));
            });



            try{
                final String seriesId = getMD5(sermon.getSeries());

                final String id = getMD5(sermon.getTitle());
                sermon.setId(id);
                sermon.setSeriesId(Optional.of(seriesId));
                searchService.save(sermon);

                final SeriesModel seriesModel =
                        new SeriesModel(seriesId, sermon.getSeries(), sermon.getImage().map(s -> URI.create(s)));

                if(seriesMap.get(seriesId) == null){
                    seriesModel.addSermon(sermon);
                    seriesMap.put(seriesId, seriesModel);
                }
                else if(!seriesMap.get(seriesId).getSermons().contains(sermon)){
                    seriesMap.get(seriesId).addSermon(sermon);
                }


            }
            catch (Exception e){
                logger.error("Unable to persist sermon", e);
            }

        });

        for (SeriesModel seriesModel : seriesMap.values()) {
            seriesService.save(seriesModel);
        }



    }

    private String getMD5(final String data) throws NoSuchAlgorithmException {


        final MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(data.getBytes());

        byte byteData[] = md.digest();

        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    private String convertToURLEscapingIllegalCharacters(String string){
        try {
            String decodedURL = URLDecoder.decode(string, "UTF-8");
            URL url = new URL(decodedURL);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            return uri.toURL().toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


}
