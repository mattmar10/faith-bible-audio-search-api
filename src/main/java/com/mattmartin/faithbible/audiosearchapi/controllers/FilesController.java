package com.mattmartin.faithbible.audiosearchapi.controllers;

import com.mattmartin.faithbible.audiosearchapi.dtos.MP3File;
import com.mattmartin.faithbible.audiosearchapi.http.FBCApiResponse;

import java.util.List;

import com.mattmartin.faithbible.audiosearchapi.services.S3Service;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@CrossOrigin
public class FilesController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private S3Service s3Service;

    @Autowired
    public FilesController(final S3Service s3Service){
        this.s3Service = s3Service;
    }

    @ResponseStatus(OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
    })
    @RequestMapping(method = RequestMethod.GET,
            value = "/audiofiles/unmapped",
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<FBCApiResponse<List<MP3File>>> listUnmappedFiles() {
        final List<MP3File> files = s3Service.listUnmappedMP3s();

        final FBCApiResponse<List<MP3File>> response = new FBCApiResponse<>(files, HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
