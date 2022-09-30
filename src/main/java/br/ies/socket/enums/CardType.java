package br.ies.socket.enums;

import br.ies.socket.service.TaxCalculator;
import br.ies.socket.utils.DefaultParams;

import java.math.BigDecimal;

/**
 * Card types enum implements br.ies.socket.service.TaxCalculator for return a tax based on SELIC Tax
 */
public enum CardType implements TaxCalculator {
    DEBIT{
        @Override
        public BigDecimal calculateTax(){
            return DefaultParams.SELIC_TAX.multiply(BigDecimal.valueOf(0.20));
        }
    },
    CREDIT{
        @Override
        public BigDecimal calculateTax(){
            return DefaultParams.SELIC_TAX.multiply(BigDecimal.valueOf(0.45)) ;
        }
    }
}
