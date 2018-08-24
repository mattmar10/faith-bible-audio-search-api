package com.mattmartin.faithbible.audiosearchapi.services;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class FTPServiceTest {


    private FTPService ftpService;

    @Before
    public void setUp() {
        this.ftpService = new FTPService();
    }

    @Test
    public void testFTPConnect(){

        ftpService.listUnmappedFiles();
        assertThat(1, is(1));
    }
}
