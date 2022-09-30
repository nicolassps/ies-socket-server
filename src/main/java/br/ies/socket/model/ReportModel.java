package br.ies.socket.model;

import br.ies.socket.enums.CardType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ReportModel {
    private CardType type;
    private BigDecimal amount;
    private BigDecimal fare;
}
