package br.ies.socket.model;

import br.ies.socket.enums.CardType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class SaleRequest {
    private String cardNumber;
    private BigDecimal amount;
    private CardType type;
    private String establishmentActivity;

}
