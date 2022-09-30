package br.ies.socket.utils;

import br.ies.socket.enums.CardType;
import br.ies.socket.model.ReportModel;
import br.ies.socket.repository.FileRepository;
import br.ies.socket.server.Report;
import br.ies.socket.singleton.core.SingletonEngine;
import lombok.extern.java.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.logging.Level.SEVERE;

@Log
public class FileUtils {

    public static List<ReportModel> getReport(){
        File file = SingletonEngine.getInstance(FileRepository.class).getOutputFile();
        List report;

        try {
            var fis = new FileInputStream(file);

            var content = new String(fis.readAllBytes());
            fis.close();

            if(content.isBlank())
                return Collections.emptyList();

            report = Arrays.stream(content
                    .split(System.lineSeparator()))
                    .map(line -> line.split(";"))
                    .map(line -> ReportModel
                            .builder()
                            .type(CardType.valueOf(line[2]))
                            .amount(new BigDecimal(line[1]))
                            .fare(new BigDecimal(line[3]))
                            .build())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return report;
    }

    public static void writeOnFile(File file, List<String> lines){
        try {
            var fos = new FileOutputStream(file, Boolean.TRUE);

            lines
                .stream()
                .map(l -> l.concat(System.lineSeparator()))
                .forEach(l -> FileUtils.writeOutputStream(fos, l));
        } catch (IOException e) {
            log.log(SEVERE, "Error on write file {0}", file.getName());
        }
    }

    private static void writeOutputStream(FileOutputStream os, String line){
        try{
            os.write(line.getBytes());
        } catch (IOException e) {
            log.log(SEVERE, null, e);
        }
    }
}
