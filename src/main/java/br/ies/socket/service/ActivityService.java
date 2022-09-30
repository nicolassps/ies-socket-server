package br.ies.socket.service;

import br.ies.socket.enums.EstablishmentActivity;
import br.ies.socket.singleton.annotation.Singleton;

import java.util.Arrays;
import java.util.List;

@Singleton
public class ActivityService {
    private static final List<EstablishmentActivity> ENABLED_ACTIVITY = Arrays.asList(EstablishmentActivity.FOOD, EstablishmentActivity.PRODUCTS);

    public Boolean validateActivity(String establishmentActivity){
        var activity = EstablishmentActivity.getActivityByPrefix(establishmentActivity);

        return ENABLED_ACTIVITY.contains(activity);
    }
}
