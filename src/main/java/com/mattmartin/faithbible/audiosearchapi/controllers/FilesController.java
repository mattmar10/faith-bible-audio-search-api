package com.mattmartin.faithbible.audiosearchapi.controllers;

import com.mattmartin.faithbible.audiosearchapi.dtos.MP3File;
import com.mattmartin.faithbible.audiosearchapi.dtos.Series;
import com.mattmartin.faithbible.audiosearchapi.http.FBCApiResponse;
import com.mattmartin.faithbible.audiosearchapi.services.FTPService;
import java.util.List;

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

    private FTPService ftpService;

    @Autowired
    public FilesController(final FTPService ftpService){
        this.ftpService = ftpService;
    }

    @ResponseStatus(OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
    })
    @RequestMapping(method = RequestMethod.GET,
            value = "/audiofiles/unmapped",
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<FBCApiResponse<List<MP3File>>> listUnmappedFiles() {
        final List<MP3File> files = ftpService.listUnmappedFiles();

        final FBCApiResponse<List<MP3File>> response = new FBCApiResponse<>(files, HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
