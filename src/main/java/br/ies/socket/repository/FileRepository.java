package br.ies.socket.repository;

import br.ies.socket.singleton.annotation.Singleton;
import lombok.extern.java.Log;

import java.io.File;
import java.io.IOException;

import static java.util.logging.Level.SEVERE;

/**
 * FileRepository is started on startup system for allocated a temp data storage on user device.
 */
@Singleton
@Log
public class FileRepository {
    private File saleOutFile;

    public FileRepository(){
        try {
            saleOutFile = File.createTempFile("sale", "out");
        } catch (IOException e) {
            log.log(SEVERE, null, e);
        }
    }

    public File getOutputFile(){
        return  saleOutFile;
    }
}
