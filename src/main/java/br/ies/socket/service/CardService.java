package br.ies.socket.service;

import br.ies.socket.exception.InvalidCardException;
import br.ies.socket.singleton.annotation.Inject;
import br.ies.socket.singleton.annotation.Singleton;
import br.ies.socket.utils.Patterns;

import java.util.Arrays;
import java.util.stream.Collectors;

import static java.lang.Boolean.FALSE;

@Singleton
public class CardService {

    public Boolean validateCard(String cardNumber){
        if(FALSE.equals(cardNumber.length() == 16))
            throw new InvalidCardException("Invalid Card Number length");

        if(Patterns.NON_DIGIT.matcher(cardNumber).find())
            throw new InvalidCardException("Invalid Card Number, ");

        if(Patterns.REPEATED_CHARACTERS_TWO_TIMES.matcher(cardNumber).find())
            throw new InvalidCardException("Invalid Card Number, repeated numbers");

        var sequence = cardNumber.substring(12, 16);
        var arr = Arrays
                .stream(sequence.split(""))
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        return arr.get(0) + arr.get(1) > arr.get(3);
    }
}
