package br.ies.socket.model;

import br.ies.socket.enums.CardType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Builder
@Getter
public class SalePersist {
    private String cardNumber;
    private BigDecimal amount;
    private CardType type;
    private BigDecimal fare;

    public List<String> prepareLineForPersist(){
        return Arrays.asList(
                cardNumber,
                amount.toPlainString(),
                type.name(),
                fare.toPlainString()
        );
    }
}
