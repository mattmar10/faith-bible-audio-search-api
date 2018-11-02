package com.mattmartin.faithbible.audiosearchapi.services;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.mattmartin.faithbible.audiosearchapi.dtos.MP3File;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class S3Service {

    private static final String BUCKET_NAME = "fbc-media";
    private static final String FOLDER = "ftp/media_repository";
    private static final String TEMP_PATH = FOLDER + "/temp/";
    private static final String RESOURCES_PATH = FOLDER + "/resources";
    private static final String ARCHIVE_PATH = FOLDER + "/archive";


    public List<MP3File> listUnmappedMP3s(){

        final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new EnvironmentVariableCredentialsProvider())
                .build();

        ListObjectsV2Result result = s3.listObjectsV2(BUCKET_NAME, TEMP_PATH);

        final List<MP3File> s3Files = new ArrayList<>();

        for(final S3ObjectSummary o: result.getObjectSummaries()){
            final String name = o.getKey().substring(o.getKey().lastIndexOf("/") + 1).trim();

            if(name.toLowerCase().endsWith(".mp3")){

                s3Files.add(new MP3File(name, s3.getUrl(BUCKET_NAME, o.getKey()).toExternalForm(), o.getKey()));

            }
        }

        return s3Files;
    }
}
