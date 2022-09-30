package br.ies.socket;

import br.ies.socket.service.TaxCalculator;
import br.ies.socket.utils.DefaultParams;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.Socket;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Client {
    private static final Random RANDOM = new Random();

    static class ClientSocket implements Runnable{

        @Override
        public void run() {
            try {
                var cliente = new Socket("127.0.0.1",3030);

                cliente.getOutputStream().write(generateData().getBytes());
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    enum CardType {
        DEBIT,
        CREDIT
    }

    public static String generateData(){
        String cardNumber = "";
        for (int i =0; i<16; i++){
            cardNumber = cardNumber.concat(randomNumber(10L).toString());
        }

        String cardType = randomNumber(2L) == 0 ? CardType.CREDIT.toString() : CardType.DEBIT.toString();
        String amount = randomNumber(300000L) + "." + randomNumber(2000L);
        String activity = "3003";

        return Arrays
                .asList(cardNumber, amount, cardType, activity)
                .stream()
                .collect(Collectors.joining(";"));
    }

    public static Long randomNumber(Long max){
        return RANDOM.nextLong(0, max);
    }

    public static void main(String[] args) {
        new Thread(new ClientSocket()).run();
    }
}
