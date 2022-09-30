package br.ies.socket.repository;

import br.ies.socket.model.SalePersist;
import br.ies.socket.model.SaleRequest;
import br.ies.socket.singleton.annotation.Inject;
import br.ies.socket.singleton.annotation.Singleton;
import br.ies.socket.utils.FileUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

@Singleton
public class SaleRepository {

    @Inject
    private FileRepository fileRepository;

    public void save(SalePersist sale){
        String line = sale.prepareLineForPersist()
                .stream()
                .collect(Collectors.joining(";"));

        FileUtils.writeOnFile(fileRepository.getOutputFile(), Arrays.asList(line));
    }
}
