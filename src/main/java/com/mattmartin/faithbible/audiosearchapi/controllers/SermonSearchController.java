package com.mattmartin.faithbible.audiosearchapi.controllers;

import com.mattmartin.faithbible.audiosearchapi.db.models.SeriesDBModel;
import com.mattmartin.faithbible.audiosearchapi.db.models.SermonDBModel;
import com.mattmartin.faithbible.audiosearchapi.dtos.Sermon;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.models.*;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.services.ESSeriesService;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.services.ESSermonService;
import com.mattmartin.faithbible.audiosearchapi.services.SeriesService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.mattmartin.faithbible.audiosearchapi.config.FaithDateTimeFormatter.DATE_FORMAT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@CrossOrigin
@RestController
public class SermonSearchController {

    private static final Logger logger = LoggerFactory.getLogger(SermonSearchController.class);

    private final ESSermonService searchService;
    private final ESSeriesService eseriesService;
    private final SeriesService seriesService;

    @Autowired
    public SermonSearchController(final ESSermonService sService,
                                  final ESSeriesService eSeriesService,
                                  final SeriesService seriesService){
        this.searchService = sService;
        this.eseriesService = eSeriesService;
        this.seriesService = seriesService;
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
    @RequestMapping(method = RequestMethod.POST, value = "/admin/indexData")
    public void indexData(@RequestBody AudioJsonSource audioJsonSource){

        searchService.deleteAll();
        eseriesService.deleteAll();

        seriesService.getAll().forEach(seriesDBModel -> {

            seriesDBModel.getSermons().forEach(sermon -> {

                try {
                    final SermonDocumentModel documentModel =
                            new SermonDocumentModel(
                                    sermon.getId(),
                                    sermon.getTitle(),
                                    sermon.getSlug(),
                                    null,
                                    sermon.getSpeaker(),
                                    sermon.getDate(),
                                    sermon.getSeries().getTitle(),
                                    new SermonMediaModel(sermon.getPdfUrl(), sermon.getMp3Url()),
                                    Optional.of(seriesDBModel.getId()),
                                    Optional.of(new StatsModel(sermon.getPlays(), sermon.getLikes(), sermon.getShares())),
                                    Optional.ofNullable(sermon.getImageUrl()),
                                    sermon.getTags() == null ? Optional.empty() : Optional.of(new HashSet(sermon.getTags()))
                            );

                    searchService.save(documentModel);
                }
                catch(Exception e){
                    logger.error(String.format("Error persisting sermon %s to index", sermon.getId()), e);
                }
            });

            try {
                final Optional<URI> imageURI = (seriesDBModel.getImageURL()) == null ? Optional.empty() : Optional.of(URI.create(seriesDBModel.getImageURL()));
                final SeriesModel seriesModel =
                        new SeriesModel(
                                seriesDBModel.getId(),
                                seriesDBModel.getTitle(),
                                seriesDBModel.getSlug(),
                                imageURI,
                                seriesDBModel.getTags() == null ? Optional.empty() : Optional.of(new HashSet(seriesDBModel.getTags()))
                        );
                eseriesService.save(seriesModel);
            }
            catch (Exception e){
                logger.error(String.format("Error persisting sermon %s to index", seriesDBModel.getId()), e);
            }

        });
    }

    @ResponseStatus(OK)
    @RequestMapping(method = RequestMethod.POST, value = "/refreshData")
    public void refreshData(@RequestBody AudioJsonSource audioJsonSource){
        logger.info(String.format("Refreshing data from source %s", audioJsonSource));

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<SermonDocumentModel[]> response =
                restTemplate.getForEntity(audioJsonSource.getUrlToJson(), SermonDocumentModel[].class);

        logger.info("Response from data source " +response.getStatusCode());

        Map<String, SeriesModel> seriesMap = new HashMap<>();

        int seriesCounter = 1;
        int sermonCounter = 1;

        for (final SermonDocumentModel sermon: response.getBody()) {
            sermon.setId(sermonCounter++);

            if(sermon.getTitle().contains("(")){
                sermon.setTitle(sermon.getTitle().substring(0, sermon.getTitle().lastIndexOf("(") - 1).trim());
            }

            sermon.getImage().ifPresent(s -> {

                String encoded = convertToURLEscapingIllegalCharacters(s);

                sermon.setImage(Optional.ofNullable(encoded));
            });



            try{
                final String seriesId = getMD5(sermon.getSeries());

               // final String id = getMD5(sermon.getTitle());
               // sermon.setId(id);
                //sermon.setSeriesId(Optional.of(seriesId));
                //searchService.save(sermon);

                final Optional<URI> imageURI = sermon.getImage().map(s -> URI.create(s));
                final StatsModel stats = new StatsModel(0, 0, 0);


                final SeriesModel seriesModel =
                        new SeriesModel(1,
                                sermon.getSeries(),
                                sermon.getSeries().replaceAll(" ", "+").replaceAll(":", "+"),
                                new ArrayList<SermonDocumentModel>(),
                                imageURI,
                                Optional.of(stats),
                                Optional.empty());

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

        };

        final List<SeriesDBModel> seriesDBModels = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

        for (SeriesModel seriesModel : seriesMap.values()) {

            final List<SermonDBModel> sermons = seriesModel.getSermons()
                    .stream()
                    .map(sermonDocumentModel -> {
                        final SermonDBModel dbModel = new SermonDBModel();
                        dbModel.setTitle(sermonDocumentModel.getTitle());
                        dbModel.setSlug(
                                dbModel.getTitle()
                                        .replaceAll("[^\\p{Alpha}\\p{Digit}]+","+").toLowerCase());
                        dbModel.setDate(sermonDocumentModel.getDate());

                        if(sermonDocumentModel.getImage().isPresent()){
                            dbModel.setImageUrl(sermonDocumentModel.getImage().get());
                        }

                        dbModel.setSpeaker(sermonDocumentModel.getSpeaker());

                        dbModel.setMapped(false);
                        dbModel.setLikes(0);
                        dbModel.setPlays(0);
                        dbModel.setShares(0);

                        dbModel.setCreatedDate(LocalDate.now());
                        dbModel.setLastUpdatedDate(LocalDate.now());

                        final SermonMediaModel mediaModel = sermonDocumentModel.getMedia();

                        mediaModel.getMp3().ifPresent(mp3URL -> dbModel.setMp3Url(mp3URL));
                        mediaModel.getPdf().ifPresent(pdfURL -> dbModel.setPdfUrl(pdfURL));

                        return dbModel;
            }).collect(Collectors.toList());

            final SeriesDBModel seriesDBModel = new SeriesDBModel();
            seriesDBModel.setTitle(seriesModel.getTitle());

            String url = "";

            for(SermonDBModel sermon: sermons){
                if(sermon.getImageUrl() != null){
                    url = sermon.getImageUrl();
                    break;
                }
            }

            seriesDBModel.setImageURL(url);
            seriesDBModel.setMapped(false);
            seriesDBModel.setLikes(0);
            seriesDBModel.setPlays(0);
            seriesDBModel.setShares(0);

            seriesDBModel.setCreatedDate(LocalDate.now());
            seriesDBModel.setLastUpdatedDate(LocalDate.now());

            seriesDBModel.setSermons(sermons);

            seriesDBModel.setSlug(seriesDBModel.getTitle().replaceAll("[^\\p{Alpha}\\p{Digit}]+", "+").toLowerCase());

            final SeriesDBModel saved = seriesService.save(seriesDBModel);
            seriesDBModels.add(saved);

        }

        logger.info("Pushing to elastic search");

        seriesDBModels.forEach(series -> {
            series.getSermons().forEach(sermon -> {

                final SermonDocumentModel documentModel =
                        new SermonDocumentModel(
                                sermon.getId(),
                                sermon.getTitle(),
                                sermon.getSlug(),
                                null,
                                sermon.getSpeaker(),
                                sermon.getDate(),
                                sermon.getSeries().getTitle(),
                                new SermonMediaModel(sermon.getPdfUrl(), sermon.getMp3Url()),
                                Optional.of(series.getId()),
                                Optional.of(new StatsModel(0, 0, 0)),
                                Optional.ofNullable(sermon.getImageUrl()),
                                Optional.of(new HashSet<>())
                                );
                searchService.save(documentModel);

            });
            final Optional<URI> imageURI = (series.getImageURL()) == null ? Optional.empty() : Optional.of(URI.create(series.getImageURL()));
            final SeriesModel seriesModel =
                    new SeriesModel(series.getId(), series.getTitle(), series.getSlug(), imageURI, Optional.of(new HashSet<>()));
            eseriesService.save(seriesModel);
        });

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
