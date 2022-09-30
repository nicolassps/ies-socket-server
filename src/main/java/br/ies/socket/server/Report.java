package br.ies.socket.server;

import br.ies.socket.enums.CardType;
import br.ies.socket.model.ReportModel;
import br.ies.socket.utils.FileUtils;
import lombok.extern.java.Log;

import java.io.File;
import java.math.BigDecimal;
import java.util.TimerTask;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.WARNING;

@Log
public class Report extends TimerTask {

    @Override
    public void run() {
        try {
            var report = FileUtils.getReport();

            if(report.isEmpty()){
                log.log(INFO, "No sales at moment");
                return;
            }

            var creditReport = ReportModel
                    .builder()
                    .type(CardType.CREDIT)
                    .amount(BigDecimal.ZERO)
                    .fare(BigDecimal.ZERO)
                    .build();

            var debitReport = ReportModel
                    .builder()
                    .type(CardType.CREDIT)
                    .amount(BigDecimal.ZERO)
                    .fare(BigDecimal.ZERO)
                    .build();

            report
                .stream()
                .filter(r -> r.getType().equals(CardType.CREDIT))
                .forEach(r -> {
                    creditReport.setAmount(creditReport.getAmount().add(r.getAmount()));
                    creditReport.setFare(creditReport.getFare().add(r.getFare()));
                });

            report
                .stream()
                .filter(r -> r.getType().equals(CardType.DEBIT))
                .forEach(r -> {
                    debitReport.setAmount(debitReport.getAmount().add(r.getAmount()));
                    debitReport.setFare(debitReport.getFare().add(r.getFare()));
                });

            String message = "TOTAL SALE\n";
            message += "CREDIT VALUE " + creditReport.getAmount().toPlainString() + " | RECEIPT " + creditReport.getFare().toPlainString() + "\n";
            message += "DEBIT VALUE " + debitReport.getAmount().toPlainString() + " | RECEIPT " + debitReport.getFare().toPlainString() + "\n";

            log.log(INFO, message);
        } catch (Exception e) {
            log.log(WARNING, "Error on summary report", e.getMessage());
        }
    }
}
