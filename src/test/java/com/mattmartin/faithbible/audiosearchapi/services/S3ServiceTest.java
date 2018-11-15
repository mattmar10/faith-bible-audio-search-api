package com.mattmartin.faithbible.audiosearchapi.services;

import com.mattmartin.faithbible.audiosearchapi.dtos.MP3File;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class S3ServiceTest {

    @Mock
    private S3Service s3Service = mock(S3Service.class);

    @Test
    public void testS3Connect(){

        final List<MP3File> files = new ArrayList<MP3File>();

        files.add(new MP3File("aname", "http://adomain.com/url", "as3Key"));

        when(s3Service.listUnmappedMP3s()).thenReturn(files);
        final List<MP3File> returned = s3Service.listUnmappedMP3s();
        assertThat(returned, is(files));
    }
}
