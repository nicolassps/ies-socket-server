package br.ies.socket.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum EstablishmentActivity {
    FOOD("100"),
    SERVICES("200"),
    PRODUCTS("300");

    String prefix;

    public static EstablishmentActivity getActivityByPrefix(String establishmentActivity){
        return Arrays
                .stream(EstablishmentActivity.values())
                .filter(activity -> establishmentActivity.startsWith(activity.getPrefix()))
                .findFirst()
                .orElseThrow(IllegalAccessError::new);
    }
}
