package br.ies.socket.server;

import br.ies.socket.enums.CardType;
import br.ies.socket.exception.IllegalDataException;
import br.ies.socket.model.SaleRequest;
import br.ies.socket.service.SaleService;
import br.ies.socket.singleton.core.SingletonEngine;
import lombok.extern.java.Log;

import java.math.BigDecimal;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

@Log
public class Processor implements Runnable{
    private byte[] data;
    private final SaleService saleService;

    public Processor(byte[] data){
        this.data = data;
        saleService = SingletonEngine.getInstance(SaleService.class);
    }

    @Override
    public void run() {
        log.log(INFO, "Starting sale processing");
        try {
            var line = new String(data);
            var values = line.split(";");

            if(values.length != 4)
                throw new IllegalDataException();

            var sale = SaleRequest
                    .builder()
                    .cardNumber(values[0])
                    .amount(new BigDecimal(values[1]))
                    .type(CardType.valueOf(values[2]))
                    .establishmentActivity(values[3])
                    .build();

            log.log(INFO, "Sale in card {0}", sale.getCardNumber());

            saleService.doSale(sale);
        } catch (Exception e){
            log.log(WARNING, e.getMessage());
        }
    }
}
