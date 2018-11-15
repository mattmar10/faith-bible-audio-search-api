package com.mattmartin.faithbible.audiosearchapi.services;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.mattmartin.faithbible.audiosearchapi.dtos.MP3File;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class S3Service {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    private static final String BUCKET_NAME = "fbc-media";
    private static final String FOLDER = "ftp/media_repository";
    private static final String TEMP_PATH = FOLDER + "/temp/";
    private static final String RESOURCES_PATH = FOLDER + "/resources/";
    private static final String ARCHIVE_PATH = FOLDER + "/archive/";

    private static final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
            .withCredentials(new EnvironmentVariableCredentialsProvider())
            .build();

    public List<MP3File> listUnmappedMP3s(){

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

    public String getURL(final S3ObjectSummary object){
        return s3.getUrl(object.getBucketName(), object.getKey()).toExternalForm();
    }

    public Optional<String> findImageURL(final String name){

        ListObjectsV2Result result = s3.listObjectsV2(BUCKET_NAME, RESOURCES_PATH + "mp3-images/" + name);

        return result.getObjectSummaries()
                .stream()
                .filter(o -> o.getKey().endsWith(".jpg") || o.getKey().endsWith(".jpeg") || o.getKey().endsWith(".png"))
                .findFirst()
                .map(f -> s3.getUrl(f.getBucketName(), f.getKey()).toExternalForm());

    }

    public Optional<S3ObjectSummary> getUnmappedMP3(final String name){
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new EnvironmentVariableCredentialsProvider())
                .build();

        ListObjectsV2Result result = s3.listObjectsV2(BUCKET_NAME, TEMP_PATH + name);

        return result.getObjectSummaries()
                .stream()
                .filter(o -> o.getKey().endsWith(".mp3"))
                .findFirst();

    }

    public Optional<S3ObjectSummary> getUnmappedPDF(final String name){
        ListObjectsV2Result result = s3.listObjectsV2(BUCKET_NAME, TEMP_PATH + name);

        return result.getObjectSummaries()
                .stream()
                .filter(o -> o.getKey().endsWith(".pdf"))
                .findFirst();

    }


    public boolean archiveSermon(final String name){

        final Optional<S3ObjectSummary> mp3 = getUnmappedMP3(name);

        if(!mp3.isPresent()){
            logger.warn(String.format("Unable to find mp3 for [%s]", name));
            return false;
        }

        final Optional<S3ObjectSummary> pdf = getUnmappedPDF(name);

        if(!pdf.isPresent()){
            logger.warn(String.format("Unable to find pdf for [%s]", name));
        }

        // Copy the object into a new object in the same bucket.
        final S3ObjectSummary mp3Object = mp3.get();
        final String localPart = mp3Object.getKey().substring(mp3Object.getKey().lastIndexOf("/" + 1));

        final CopyObjectRequest copyMP3Request =
                new CopyObjectRequest(BUCKET_NAME, mp3Object.getKey(), BUCKET_NAME, ARCHIVE_PATH + localPart);

        //if there is a pdf, setup a request to copy it
        final Optional<CopyObjectRequest> pdfCopyMaybe = pdf.map(obj -> {
            final String imgName = obj.getKey().substring(obj.getKey().lastIndexOf("/" + 1));
            return new CopyObjectRequest(BUCKET_NAME, obj.getKey(), BUCKET_NAME, ARCHIVE_PATH + imgName);
        });

        logger.info(String.format("Copying object(s) to archive for [%s]", name));
        s3.copyObject(copyMP3Request);
        pdfCopyMaybe.ifPresent(req -> s3.copyObject(req));

        //now delete
        s3.deleteObject(BUCKET_NAME, mp3Object.getKey());

        pdfCopyMaybe.ifPresent(pdfO -> s3.deleteObject(BUCKET_NAME, pdfO.getSourceKey()));

        return true;
    }
}
