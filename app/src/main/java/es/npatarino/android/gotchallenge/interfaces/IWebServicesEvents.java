package es.npatarino.android.gotchallenge.interfaces;

import java.util.HashMap;
import java.util.List;

import es.npatarino.android.gotchallenge.models.GoTCharacter;
import es.npatarino.android.gotchallenge.models.GoTHouse;
import es.npatarino.android.gotchallenge.services.JsonObject;

public interface IWebServicesEvents {
    void onGetDataFinishCallback(List<JsonObject> result);
    void onGetDataErrorCallback(String msg);
}
