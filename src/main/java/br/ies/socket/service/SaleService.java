package br.ies.socket.service;

import br.ies.socket.exception.ActivityNotSupportedException;
import br.ies.socket.exception.InvalidCardException;
import br.ies.socket.model.SalePersist;
import br.ies.socket.model.SaleRequest;
import br.ies.socket.repository.SaleRepository;
import br.ies.socket.singleton.annotation.Inject;
import br.ies.socket.singleton.annotation.Singleton;

import java.math.BigDecimal;

import static java.lang.Boolean.FALSE;

@Singleton
public class SaleService {

    @Inject
    private CardService cardService;

    @Inject
    private ActivityService activityService;

    @Inject
    private SaleRepository saleRepository;

    public void doSale(SaleRequest sale){
        if(FALSE.equals(cardService.validateCard(sale.getCardNumber())))
            throw new InvalidCardException("Security check to card number failed");

        if(FALSE.equals(activityService.validateActivity(sale.getEstablishmentActivity())))
            throw new ActivityNotSupportedException();

        BigDecimal fare = sale.getAmount().multiply(sale.getType().calculateTax());

        saleRepository.save(SalePersist
                .builder()
                .cardNumber(sale.getCardNumber())
                .type(sale.getType())
                .amount(sale.getAmount())
                .fare(fare)
                .build());
    }

}
