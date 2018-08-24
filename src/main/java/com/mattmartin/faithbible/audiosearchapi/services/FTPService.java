package com.mattmartin.faithbible.audiosearchapi.services;

import com.mattmartin.faithbible.audiosearchapi.dtos.MP3File;
import org.apache.commons.net.ftp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class FTPService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String FTP_SERVER = "synology.faithbibleok.com";
    private static final int FTP_PORT = 21;

    public List<MP3File> listUnmappedFiles(){

        final String user = System.getenv("FTP_USER");
        String pass = System.getenv("FTP_PASS");
        FTPSClient ftpClient = new FTPSClient();

        try {
            ftpClient.connect(FTP_SERVER, FTP_PORT);
            final boolean loggedIn = ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();

            final String pathname = "/media_repository/temp";
            final FTPFile[] files =
                    ftpClient.listFiles(pathname, (f) -> f.isFile() && f.getName().endsWith(".mp3"));

            final List<MP3File> mp3Files = new ArrayList<>();

            for(final FTPFile file: files){
                mp3Files.add(new MP3File(file.getName(), pathname + '/' + file.getName()));
            }

            return mp3Files;

        } catch (IOException e) {
            logger.error("Unable to list unmapped files", e);
        }

        return Collections.emptyList();
    }
}
